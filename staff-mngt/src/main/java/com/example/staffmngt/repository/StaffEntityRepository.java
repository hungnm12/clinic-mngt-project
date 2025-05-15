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


    @Query("select s from StaffEntity  s where s.firstName like :staffName " +
            "or s.lastName like :staffName")
    StaffEntity findByStaffName(String staffName);


    StaffEntity findByEmail(String email);


    List<StaffEntity> getListStaffBySpecialty(String specialty);

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
            "s.lastName, s.firstName, s.age, s.role,s.phone,s.specialty,s.email, " +
            "s.staffCode, s.password, s.status) " +
            "from StaffEntity s " +
            "where (:staffCode is null or :staffCode = '' or s.staffCode like concat('%', :staffCode, '%')) " +
            "and (:lastName is null or :lastName = '' or s.lastName like concat('%', :lastName, '%')) " +
            "and (:firstName is null or :firstName = '' or s.firstName like concat('%', :firstName, '%')) " +
            "and (:email is null or :email = '' or s.email like concat('%', :email, '%')) " +
            "and (:role is null or :role = '' or s.role like concat('%', :role, '%')) " +
            "and (:phone is null or :phone = '' or s.phone like concat('%', :phone, '%')) " +
            "and (:specialty is null or :specialty = '' or s.specialty like concat('%', :specialty, '%'))")
    Page<StaffResDto> getListOfStaff(
            @Param("lastName") String lastName,
            @Param("firstName") String firstName,
            @Param("email") String email,
            @Param("staffCode") String staffCode,
            @Param("role") String role,
            @Param("phone") String phone,
            @Param("specialty") String specialty,
            Pageable pageable);


}
