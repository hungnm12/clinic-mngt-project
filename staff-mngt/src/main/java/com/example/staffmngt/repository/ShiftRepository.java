package com.example.staffmngt.repository;

import com.example.staffmngt.entity.ServiceEntity;
import com.example.staffmngt.entity.ShiftScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftScheduleEntity, Long> {

    ShiftScheduleEntity findShiftScheduleByShiftCode(String shiftCode);
}
