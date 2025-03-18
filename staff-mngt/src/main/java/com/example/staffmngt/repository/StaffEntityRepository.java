package com.example.staffmngt.repository;

import com.example.staffmngt.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
