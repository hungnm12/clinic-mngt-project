package com.example.staffmngt.service.Impl;

import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.constant.StaffConstant;
import com.example.staffmngt.dto.req.*;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.entity.StaffHistoryEntity;
import com.example.staffmngt.enumSt.Role;
import com.example.staffmngt.rabbitmq.RabbitMqProducer;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.repository.StaffHistoryRepository;
import com.example.staffmngt.service.StaffService;
import com.example.staffmngt.utils.ConvertUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffEntityRepository staffEntityRepository;

    @Autowired
    private StaffHistoryRepository staffHistoryRepository;

    @Autowired
    private RabbitMqProducer rabbitMqProducer;
    @Value("${rabbitmq.queue.name}")
    private String queueName;


    @Override
    public GeneralResponse addStaff(StaffReqDto staff) {
        log.info("Adding staff: {}", staff);

        if (staff == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }

        String tenantId = TenantContext.getTenant();
        if (tenantId == null || tenantId.isEmpty()) {
            log.error("[addStaff] Tenant ID is missing!");
            throw new IllegalStateException("Tenant ID is missing!");
        }
        log.info("[addStaff] Tenant ID: {}", tenantId);

        String staffCode = "Staff_" + new Random().nextInt(100);
        if (staffEntityRepository.findByEmail(staff.getEmail()) != null) {
            return new GeneralResponse(HttpStatus.CONFLICT.value(), "", "Staff already exists!", null);
        }
        if (!isValidEmail(staff.getEmail())) {
            return new GeneralResponse(HttpStatus.CONFLICT.value(), "", "Email is invalid!", null);
        }
        if (staff.getPassword() == null || !isValidPassword(staff.getPassword())) {
            return new GeneralResponse(HttpStatus.CONFLICT.value(), "", "Password is invalid!", null);
        }
        StaffHistoryEntity staff1 = StaffHistoryEntity.builder()
                .email(staff.getEmail())
                .lastName(staff.getLastName())
                .firstName(staff.getFirstName())
                .staffCode(staffCode)
                .age(staff.getAge())
                .department(staff.getDepartment())
                .password(Base64.getEncoder().encodeToString(staff.getPassword().getBytes()))
                .role(String.valueOf(Role.EMPLOYEE))
                .status(StaffConstant.PENDING)
                .build();

        try {
            AddAccReq addAccReq = AddAccReq
                    .builder()
                    .type("Staff")
                    .username(staff1.getEmail())
                    .password(staff1.getPassword())
                    .tenantId(tenantId)
                    .status(staff1.getStatus())
                    .build();


            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataMap = objectMapper.convertValue(addAccReq, new TypeReference<Map<String, Object>>() {
            });
            log.info("[addStaff] Adding staff: {}", dataMap);
            RabbitMsgReq rabbitMsgReq = RabbitMsgReq.builder()
                    .topic("add_acc")
                    .queue(queueName)
                    .data(dataMap)
                    .build();
            String message = ConvertUtils.marshalJsonAsPrettyString(rabbitMsgReq);
            rabbitMqProducer.sendCreatedAccToAccSrv(message);
            staffHistoryRepository.save(staff1);

            return new GeneralResponse(HttpStatus.CREATED.value(), "", "Staff saved.", staff1);
        } catch (Exception e) {
            log.error("Error saving staff: ", e);
            return new GeneralResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "", "Error occurred while saving staff.", null);
        }
    }

    @Override
    public GeneralResponse updateStaff(UpdReqDto staff) {
        if (staff == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        String tenantId = TenantContext.getTenant();
        if (tenantId == null) {
            log.info("[updateStaff] tenantId = null", tenantId);
            throw new IllegalStateException("Tenant ID is missing!");
        }
        StaffEntity st = staffEntityRepository.findByStaffCode(staff.getStaffCode());
        if (st == null) {
            return null;
        }
        StaffEntity updStff = StaffEntity.builder()
                .email(staff.getEmail())
                .lastName(staff.getLastName())
                .firstName(staff.getFirstName())
                .staffCode(st.getStaffCode())
                .age(staff.getAge())
                .department(staff.getDepartment())
                .role(st.getRole())
                .build();

        Map<Object, Object> map = new HashMap<>();
        map.put(updStff, StaffResDto.class);
        return new GeneralResponse(HttpStatus.OK.value(), "", "Staff updated", updStff);
    }

    @Override
    public GeneralResponse deleteStaff(String staffCode) {
        if (staffCode == null) {
            return new GeneralResponse();
        }

        String tenantId = TenantContext.getTenant();
        if (tenantId == null) {
            log.info("[deleteStaff] tenantId = null", tenantId);
            throw new IllegalStateException("Tenant ID is missing!");
        }
        StaffEntity st = staffEntityRepository.findByStaffCode(staffCode);
        if (st == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        staffEntityRepository.delete(st);
        return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff deleted", null);

    }


    @Override
    public GeneralResponse searchStaff(UpdReqDto updReqDto) {
        if (updReqDto == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        if (updReqDto.getStaffCode() == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        String tenantId = TenantContext.getTenant();
        if (tenantId == null) {
            log.info("[searchStaff] tenantId = null", tenantId);
            throw new IllegalStateException("Tenant ID is missing!");
        }
        List<StaffEntity> lstStff = staffEntityRepository.searchStaff(updReqDto.getStaffCode(),
                updReqDto.getFirstName(), updReqDto.getEmail(), updReqDto.getLastName(),
                updReqDto.getRole(), updReqDto.getDepartment());

        return new GeneralResponse(HttpStatus.OK.value(), "", "Staff searched", lstStff);
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.length() < 5) {
            return false;
        }
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecialChar = false;

        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
        Matcher lowercaseMatcher = lowercasePattern.matcher(password);
        Matcher specialCharMatcher = specialCharPattern.matcher(password);


        hasUppercase = uppercaseMatcher.find();
        hasLowercase = lowercaseMatcher.find();
        hasSpecialChar = specialCharMatcher.find();

        return hasUppercase && hasLowercase && hasSpecialChar;

    }


    public void processResponseStaffFromQueue(RabbitMsgReq rabbitMsgReq) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert Map data to StaffResDto
            StaffResDto staffResDto = objectMapper.convertValue(rabbitMsgReq.getData(), StaffResDto.class);

            // Build StaffEntity using the converted DTO
            StaffEntity staffEntity = StaffEntity.builder()
                    .email(staffResDto.getEmail())
                    .firstName(staffResDto.getFirstName())
                    .lastName(staffResDto.getLastName())
                    .password(staffResDto.getPassword())
                    .role(staffResDto.getRole())
                    .staffCode(staffResDto.getStaffCode())
                    .department(staffResDto.getDepartment())
                    .status(staffResDto.getStatus())
                    .build();
            staffEntityRepository.save(staffEntity);

        } catch (Exception e) {
            log.error("Error processing staff response: ", e);
        }
    }

}
