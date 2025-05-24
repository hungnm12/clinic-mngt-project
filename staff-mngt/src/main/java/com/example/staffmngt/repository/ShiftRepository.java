package com.example.staffmngt.repository;


import com.example.staffmngt.entity.ServiceEntity;
import com.example.staffmngt.entity.ShiftScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftScheduleEntity, Long> {

    ShiftScheduleEntity findShiftScheduleByShiftCode(String shiftCode);


    @Query("SELECT COUNT(s), FUNCTION('DATE_FORMAT', s.bookedTime, '%Y-%m-%d') " +
            "FROM ShiftScheduleEntity s " +
            "GROUP BY FUNCTION('DATE_FORMAT', s.bookedTime, '%Y-%m-%d')")
    List<Object[]> countShiftsByDate();



}
