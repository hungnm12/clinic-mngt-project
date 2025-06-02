package com.example.staffmngt.repository;


import com.example.staffmngt.dto.res.ScheduleListResDto;
import com.example.staffmngt.entity.ServiceEntity;
import com.example.staffmngt.entity.ShiftScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftScheduleEntity, Long> {

    ShiftScheduleEntity findShiftScheduleByShiftCode(String shiftCode);
    ShiftScheduleEntity findShiftScheduleBySchedulerCode(String schedulerCode);

    List<ShiftScheduleEntity> findAllByStaffId(Long id);


    @Query(value = "select new com.example.staffmngt.dto.res.ScheduleListResDto(" +
            "l.shiftCode, " +
            "l.staff.staffCode, " +
            "l.bookedTime, " +
            "l.bookedPatient," +
            "l.note," +
            "l.status," +
            "l.schedulerCode" +
            ") " +
            "from ShiftScheduleEntity l " +
            "where l.staff.id = :staffId")
    List<ScheduleListResDto> getShiftsByStaffId(Long staffId);

    @Query("SELECT COUNT(s), FUNCTION('DATE_FORMAT', s.bookedTime, '%Y-%m-%d') " +
            "FROM ShiftScheduleEntity s " +
            "GROUP BY FUNCTION('DATE_FORMAT', s.bookedTime, '%Y-%m-%d')")
    List<Object[]> countShiftsByDate();


}
