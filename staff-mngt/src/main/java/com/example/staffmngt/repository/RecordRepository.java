package com.example.staffmngt.repository;

import com.example.staffmngt.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<RecordEntity, Long> {


}
