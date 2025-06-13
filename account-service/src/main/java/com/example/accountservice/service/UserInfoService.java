package com.example.accountservice.service;

import com.example.accountservice.config.TenantContext;
import com.example.accountservice.constant.StatusConstant;
import com.example.accountservice.dto.req.KafkaMsgRes;
import com.example.accountservice.dto.res.GeneralResponse;
import com.example.accountservice.entity.UserInfo;
//import com.example.accountservice.rabbitmq.RabbitMqProducer;
import com.example.accountservice.kafka.service.KafkaProducerService;
import com.example.accountservice.repository.UserInfoRepository;
import com.example.accountservice.utils.ConvertUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Primary
public class UserInfoService implements UserDetailsService {

    @Value("${secret.key.auth}")
    String secret;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private KafkaProducerService kafkaProducerService;

//    @Autowired
//    private RabbitMqProducer rabbitMqProducer;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(username);

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public GeneralResponse addUser(UserInfo userInfo) {
        System.out.println("hehehe ");
        TenantContext.setTenant(userInfo.getTenantId());
        if (repository.findByEmail(userInfo.getEmail()).isPresent()) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "Email already exists!", null);
        }

        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setStatus(StatusConstant.ACTIVE);
        KafkaMsgRes req = KafkaMsgRes
                .builder()
                .tenantId(userInfo.getTenantId())
                .data(userInfo)
                .build();
        repository.save(userInfo);
        String returnMsg = ConvertUtils.marshalJsonAsPrettyString(req);


        kafkaProducerService.sendSavedAccData(returnMsg);


        return new GeneralResponse(HttpStatus.SC_OK, "", "User Added Successfully", userInfo);
    }


}
