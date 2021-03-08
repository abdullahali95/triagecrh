package com.health.application.data.service;

import com.health.application.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("select p from Patient p " +
            "where lower(p.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.lastName) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(p.hospId) like lower(concat('%', :searchTerm, '%'))") //
    List<Patient> search(@Param("searchTerm") String searchTerm); //

    @Query("select p from Patient p " +
            "where (p.hospId) like (concat('%', :searchTerm, '%')) ") //
    List<Patient> search(@Param("searchTerm") int searchTerm); //
}
