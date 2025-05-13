package com.example.staffmngt.service.Impl;

import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.constant.StaffConstant;
import com.example.staffmngt.dto.req.*;
import com.example.staffmngt.dto.res.*;
import com.example.staffmngt.entity.DepartmentEntity;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.entity.StaffHistoryEntity;
import com.example.staffmngt.enumSt.Role;
import com.example.staffmngt.kafka.service.KafkaProducerService;
//import com.example.staffmngt.rabbitmq.RabbitMqProducer;
import com.example.staffmngt.repository.DepartmentRepository;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.repository.StaffHistoryRepository;
import com.example.staffmngt.service.StaffService;
import com.example.staffmngt.utils.ConvertUtils;
import com.example.staffmngt.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.aspectj.bridge.MessageUtil.fail;


@Slf4j
@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffEntityRepository staffEntityRepository;

    @Autowired
    private StaffHistoryRepository staffHistoryRepository;

    //    @Autowired
//    private RabbitMqProducer rabbitMqProducer;
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private DepartmentRepository departmentRepository;


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

        String staffCode = "Staff_" + "_" + staff.getDepartment() + new Random().nextInt(100);
        if (staffEntityRepository.findByEmail(staff.getEmail()) != null) {
            return new GeneralResponse(HttpStatus.CONFLICT.value(), "", "Staff already exists!", null);
        }
        if (!isValidEmail(staff.getEmail())) {
            return new GeneralResponse(HttpStatus.CONFLICT.value(), "", "Email is invalid!", null);
        }
        if (staff.getPassword() == null || !isValidPassword(staff.getPassword())) {
            return new GeneralResponse(HttpStatus.CONFLICT.value(), "", "Password is invalid!", null);
        }
        DepartmentEntity department = departmentRepository.findByName(staff.getDepartment());
        if (department == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Department is not found", null);
        }
        StaffHistoryEntity staff1 = StaffHistoryEntity.builder()
                .email(staff.getEmail())
                .lastName(staff.getLastName())
                .firstName(staff.getFirstName())
                .staffCode(staffCode)
                .age(staff.getAge())
                .department(department)
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
                    .staffCode(staff1.getStaffCode())
                    .build();


            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataMap = objectMapper.convertValue(addAccReq, new TypeReference<Map<String, Object>>() {
            });
            log.info("[addStaff] Adding staff: {}", dataMap);

            String message = ConvertUtils.marshalJsonAsPrettyString(addAccReq);
            log.info("sent msg {}", message);
            //rabbitMqProducer.sendCreatedAccToAccSrv(message);
            kafkaProducerService.sendAddAccReq(message);

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
        DepartmentEntity department = departmentRepository.findByName(staff.getDepartment());
        if (department == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Department is not found", null);
        }
        st.setLastName(staff.getLastName());
        st.setFirstName(staff.getFirstName());
        st.setEmail(staff.getEmail());
        st.setPassword(staff.getPassword());
        st.setRole(staff.getRole());
        st.setStaffCode(staff.getStaffCode());
        st.setDepartment(department);
        staffEntityRepository.save(st);


        return new GeneralResponse(HttpStatus.OK.value(), "", "Staff updated", st);
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

    @Override
    public GeneralResponse getListStaff(ListStaffSearchReq staffSearchReq) {
        Sort sort;
        if (!StringUtil.isNullOrEmpty(staffSearchReq.getSortBy())) {
            if (!StringUtil.isNullOrEmpty(staffSearchReq.getSortType()) && staffSearchReq.getSortType().equalsIgnoreCase("asc")) {
                sort = Sort.by(Sort.Direction.ASC, staffSearchReq.getSortBy());
            } else {
                sort = Sort.by(Sort.Direction.DESC, staffSearchReq.getSortBy());
            }
        } else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }

        int page = 0;
        if (staffSearchReq.getPage() != null) {
            page = Math.max(0, staffSearchReq.getPage() - 1);
        }

        int size = 10;
        if (staffSearchReq.getSize() != null) {
            size = staffSearchReq.getSize() < 0 ? 5 : staffSearchReq.getSize();
        }


        Pageable pageable = PageRequest.of(page, size, sort);
        Page<StaffResDto> StaffResDtoPage = staffEntityRepository.getListOfStaff(staffSearchReq.getLastName(), staffSearchReq.getFirstName(), staffSearchReq.getEmail(), staffSearchReq.getStaffCode(), staffSearchReq.getRole(), pageable);
        return new GeneralResponse(org.apache.http.HttpStatus.SC_OK, "", "service list", new ListContentPageDto<>(StaffResDtoPage, StaffResDtoPage.getContent()));
    }

    @Override
    public GeneralResponse getAllStaff() {


        return new GeneralResponse(HttpStatus.OK.value(), "", "All staff", staffEntityRepository.findAll());
    }

    @Override
    public GeneralResponse getStaff(String staffCode) {
        if (staffCode == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        StaffEntity staff = staffEntityRepository.findByStaffCode(staffCode);
        if (staff == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        return new GeneralResponse(HttpStatus.OK.value(), "", "Staff found", staff);
    }

    @Override
    public GeneralResponse getDrBySpecialty(String specialty) {
        if (specialty == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
//        //lay dsach bsi thuoc chuyen mon
//        List<StaffEntity> lst = staffEntityRepository.findAllBySpecialty(specialty);


        return null;
    }

    @Override
    public GeneralResponse getSpecialtyByDr(String staffCode) {
        return null;
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


    public void processResponseStaffFromQueue(UserInfo userInfo) {

        try {

            StaffHistoryEntity staffHistoryEntity = staffHistoryRepository.findByStaffCodeOrderByIdDesc(userInfo.getStaffCode());
            if (staffHistoryEntity == null) {
                return;
            }
            DepartmentEntity department = departmentRepository.findByName(staffHistoryEntity.getDepartment().getName());
            if (department == null) {
                return;
            }
            //New his staff upd
            StaffHistoryEntity staff1 = StaffHistoryEntity.builder()
                    .email(userInfo.getEmail())
                    .lastName(staffHistoryEntity.getLastName())
                    .firstName(staffHistoryEntity.getFirstName())
                    .staffCode(staffHistoryEntity.getStaffCode())
                    .age(staffHistoryEntity.getAge())
                    .department(department)
                    .password(userInfo.getPassword())
                    .role(userInfo.getRoles())
                    .status(userInfo.getStatus())
                    .build();

            staffHistoryRepository.save(staff1);


            // Build StaffEntity using the converted DTO
            StaffEntity staffEntity = StaffEntity.builder()
                    .age(staffHistoryEntity.getAge())
                    .email(staffHistoryEntity.getEmail())
                    .firstName(staffHistoryEntity.getFirstName())
                    .lastName(staffHistoryEntity.getLastName())
                    .password(userInfo.getPassword())
                    .role(userInfo.getRoles())
                    .staffCode(userInfo.getStaffCode())
                    .department(department)
                    .status(userInfo.getStatus())
                    .build();
            staffEntityRepository.save(staffEntity);

        } catch (Exception e) {
            log.error("Error processing staff response: ", e);
        }
    }

//    @Test
//    void givenSystem_whenUsingOSHI_thenExtractSystemUptime() {
//        SystemInfo si = new SystemInfo();
//        OperatingSystem os = si.getOperatingSystem();
//
//        long uptime = os.getSystemUptime();
//        assertTrue(uptime >= 0, "System uptime should be non-negative");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            fail("Test interrupted");
//        }
//        long newUptime = os.getSystemUptime();
//        assertTrue(newUptime >= uptime, "Uptime should increase over time");
//    }
//
//    @Test
//    void givenSystem_whenUsingOSHI_thenExtractCPUDetails() {
//        SystemInfo si = new SystemInfo();
//        CentralProcessor processor = si.getHardware().getProcessor();
//
//        assertNotNull(processor, "Processor object should not be null");
//        assertTrue(processor.getPhysicalProcessorCount() > 0, "CPU must have at least one physical core");
//        assertTrue(processor.getLogicalProcessorCount() >= processor.getPhysicalProcessorCount(),
//                "Logical cores should be greater than or equal to physical cores");
//    }
//
//    @Test
//    void givenSystem_whenUsingOSHI_thenExtractCPULoad() throws InterruptedException {
//        SystemInfo si = new SystemInfo();
//        CentralProcessor processor = si.getHardware().getProcessor();
//
//        long[] prevTicks = processor.getSystemCpuLoadTicks();
//        TimeUnit.SECONDS.sleep(1);
//        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
//
//        assertTrue(cpuLoad >= 0 && cpuLoad <= 100, "CPU load should be between 0% and 100%");
//    }
//
//    @Test
//    void givenSystem_whenUsingOSHI_thenExtractDiskDetails() {
//        SystemInfo si = new SystemInfo();
//        List<HWDiskStore> diskStores = si.getHardware().getDiskStores();
//
//        assertFalse(diskStores.isEmpty(), "There should be at least one disk");
//
//        for (HWDiskStore disk : diskStores) {
//            assertNotNull(disk.getModel(), "Disk model should not be null");
//            assertTrue(disk.getSize() >= 0, "Disk size should be non-negative");
//        }
//    }

}
