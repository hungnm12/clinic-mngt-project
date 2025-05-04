package com.example.staffmngt.repository;

import com.example.staffmngt.entity.StaffHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffHistoryRepository extends JpaRepository<StaffHistoryEntity, Long> {

    StaffHistoryEntity findByStaffCodeOrderByIdDesc(String staffCode);
}
