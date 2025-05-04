package com.example.schedulerservice.repository;

import com.example.schedulerservice.entity.SchedulerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerEntity, Long> {

    boolean existsBySchedulerCode(String schedulerCode);

}
