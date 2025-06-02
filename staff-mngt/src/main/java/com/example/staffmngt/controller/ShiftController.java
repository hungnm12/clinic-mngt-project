package com.example.staffmngt.controller;


import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.constant.ShiftStatusConstant;
import com.example.staffmngt.dto.MultiTenantsEntity;
import com.example.staffmngt.dto.req.*;
import com.example.staffmngt.dto.res.GeneralResponse;

import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.feign.AccFeignClient;
import com.example.staffmngt.feign.TenantFeignClient;
import com.example.staffmngt.kafka.service.KafkaProducerService;
import com.example.staffmngt.repository.ShiftRepository;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.service.ShiftScheduleService;
import com.example.staffmngt.utils.JsonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.tree.TreeNode;
import java.security.Key;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

@CrossOrigin
@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftScheduleService shiftScheduleService;

    @Autowired
    private AccFeignClient accFeignClient;

    @Autowired
    private StaffEntityRepository staffEntityRepository;
    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private TenantFeignClient tenantFeignClient;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @DeleteMapping("/delete")
    GeneralResponse deleteStaff(@RequestParam String schedulerCode, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);

        return shiftScheduleService.deleteShift(schedulerCode);
    }

    @PostMapping("/update")
    GeneralResponse updateStaff(@RequestBody AddShiftReq shiftSchedule, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        return shiftScheduleService.updateShift(shiftSchedule);
    }

    @PostMapping("/add")
    GeneralResponse addStaff(@RequestBody AddShiftReq shiftSchedule, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);

        MultiTenantsEntity m = tenantFeignClient.getTenant(tenantId);
        String clinicName = m.getClinicName();
        String clinicPhone = m.getPhone();
        if (shiftSchedule.getStatus().equals(ShiftStatusConstant.SHIFT_DENIED)) {
            MailInfoFromDrReqDto mailInfoReqDto = new MailInfoFromDrReqDto();
            mailInfoReqDto.setDrName(shiftSchedule.getDrName());
            mailInfoReqDto.setSubject("Appointment Reject");
            mailInfoReqDto.setAppointmentDate(shiftSchedule.getBookedTime().toString());
            mailInfoReqDto.setClinicPhone(clinicPhone);
            mailInfoReqDto.setEmailReceiver(shiftSchedule.getBookedPatient());
            mailInfoReqDto.setClinicName(clinicName);

            String msg = JsonUtils.marshalJsonAsPrettyString(mailInfoReqDto);
            try {
                kafkaProducerService.sendDeniedAppointment(msg);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                throw new RuntimeException(e);
            }

        }


        return shiftScheduleService.createShift(shiftSchedule);
    }


    @GetMapping("/all")
    GeneralResponse getAllStaff(@RequestHeader("X-Tenant-ID") String tenantId, @RequestHeader("Auth") String authHeader) {
        ResponseEntity<Boolean> re = accFeignClient.validateToken(authHeader, tenantId);
        if (!re.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        String token = authHeader.replace("Bearer ", "");
        String email = extractUsername(token);
        StaffEntity st = staffEntityRepository.findByEmail(email);
        if (st == null) {
            return null;
        }

        return shiftScheduleService.getAllShifts(st.getId());

    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode("hMGX8YeJ0hzRrB6QEzfqT2J94VKL3zQc/Mj2RVJyXzc=");
        // Ensure we have a key with at least 256 bits (32 bytes) for HS256
        if (keyBytes.length < 32) {
            throw new RuntimeException("JWT secret key must be at least 256 bits (32 bytes) when decoded from Base64. Current key is only " + (keyBytes.length * 8) + " bits.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @GetMapping("/stats")
    private List<Object[]> stats() {
        return shiftRepository.countShiftsByDate();
    }
}
