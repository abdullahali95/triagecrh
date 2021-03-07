package com.health.application.data.service;

import com.health.application.data.entity.Admission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {

}