package com.example.accountservice.service;

import com.example.accountservice.constant.StatusConstant;
import com.example.accountservice.dto.req.KafkaMsgRes;
import com.example.accountservice.dto.res.GeneralResponse;
import com.example.accountservice.entity.UserInfo;
//import com.example.accountservice.rabbitmq.RabbitMqProducer;
import com.example.accountservice.kafka.service.KafkaProducerService;
import com.example.accountservice.repository.UserInfoRepository;
import com.example.accountservice.utils.ConvertUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Primary
public class UserInfoService implements UserDetailsService {
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
        if (repository.findByEmail(userInfo.getEmail()).isPresent()) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "Email already exists!", null);
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setStatus(StatusConstant.ACTIVE);
        KafkaMsgRes req = KafkaMsgRes
                .builder()
                .data(userInfo)
                .build();

        String returnMsg = ConvertUtils.marshalJsonAsPrettyString(req);


        kafkaProducerService.sendSavedAccData(returnMsg);
        repository.save(userInfo);

        return new GeneralResponse(HttpStatus.SC_OK, "", "User Added Successfully", userInfo);
    }

}
