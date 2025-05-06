package com.example.staffmngt.repository;

import com.example.staffmngt.dto.res.ServiceResDto;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.entity.StaffEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffEntityRepository extends JpaRepository<StaffEntity, Long> {

    StaffEntity findByStaffCode(String staffCode);


    StaffEntity findByEmail(String email);


    @Query("select s from StaffEntity s where (:department is null or s.department = :department) " +

            "and (:firstName is null or s.firstName = :firstName) " +
            "and (:email is null or s.email = :email) " +
            "and (:lastName is null or s.lastName = :lastName) " +
            "and (:role is null or s.role = :role)" +
            "and s.staffCode =:staffCode")
    List<StaffEntity> searchStaff(@Param("staffCode") String staffCode,
                                  @Param("firstName") String firstName,
                                  @Param("email") String email,
                                  @Param("lastName") String lastName,
                                  @Param("role") String role,
                                  @Param("department") String department);


    @Query("select new com.example.staffmngt.dto.res.StaffResDto(" +
            "s.lastName, s.firstName, s.age, s.role, s.email,  " +
            "s.staffCode, s.password, s.status) " +
            "from StaffEntity s " +
            "where (:staffCode is null or :staffCode = '' or s.staffCode like %:staffCode%) " +
            "and (:lastName is null or :lastName = '' or s.lastName like %:lastName%) " +
            "and (:firstName is null or :firstName = '' or s.firstName like %:firstName%) " +
            "and (:email is null or :email = '' or s.email like %:email%) " +
            "and (:role is null or :role = '' or s.role like %:role%)")
    Page<StaffResDto> getListOfStaff(
            @Param("lastName") String lastName,
            @Param("firstName") String firstName,
            @Param("email") String email,
            @Param("staffCode") String staffCode,
            @Param("role") String role,
            Pageable pageable);





}
