package com.example.staffmngt.repository;

import com.example.staffmngt.dto.res.ServiceResDto;
import com.example.staffmngt.entity.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    ServiceEntity findByServiceName(String serviceName);

    ServiceEntity findByServiceCode(String serviceCode);

    @Query(value = "select new com.example.staffmngt.dto.res.ServiceResDto(" +
            "l.serviceName, " +
            "l.serviceCode, " +
            "l.price, " +
            "l.note)"+
            "from ServiceEntity l " +
            "where (:serviceName is null or :serviceName = '' or l.serviceName like %:serviceName%) " +
            "and (:serviceCode is null or :serviceCode = '' or l.serviceCode like %:serviceCode%)" +
            "and (:note is null or :note = '' or l.serviceCode like %:note%)")
    Page<ServiceResDto> getListOfServices(
            @Param("serviceName") String serviceName,
            @Param("serviceCode") String serviceCode,
            @Param("note") String note,
            Pageable pageable);


}
