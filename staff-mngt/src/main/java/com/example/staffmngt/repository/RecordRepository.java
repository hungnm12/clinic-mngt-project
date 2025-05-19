package com.example.staffmngt.repository;

import com.example.staffmngt.dto.res.RecordResDto;
import com.example.staffmngt.dto.res.ServiceResDto;
import com.example.staffmngt.entity.RecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecordRepository extends JpaRepository<RecordEntity, Long> {

    @Query(value = "select new com.example.staffmngt.dto.res.RecordResDto(" +
            "l.patientName, " +
            "l.patientPhone, " +
            "l.patientDob, " +
            "l.patientEmail," +
            "l.serviceType," +
            "l.diagnose," +
            "l.assumption," +
            "l.symptom," +
            "l.assign," +
            "l.note," +
            "l.staffCode," +
            "l.inchargedDr)" +
            "from RecordEntity l " +
            "where ( l.staffCode = :staffCode) " +
            "and (:inchargedDr is null or :inchargedDr = '' or l.inchargedDr like %:inchargedDr%)")
    Page<RecordResDto> getListRrc(
            @Param("staffCode") String staffCode,
            @Param("inchargedDr") String inchargedDr,
            Pageable pageable);
}
