package com.health.application.data.service;

import com.health.application.data.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PatientService {
    private static final Logger LOGGER = Logger.getLogger(PatientService.class.getName());
    private AdmissionRepository admissionRepository;
    private PatientRepository patientRepository;
    private WardRepository wardRepository;

    public PatientService(AdmissionRepository admissionRepository,
                            PatientRepository patientRepository, WardRepository wardRepository) {
        this.admissionRepository = admissionRepository;
        this.patientRepository = patientRepository;
        this.wardRepository = wardRepository;
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public long count() {
        return patientRepository.count();
    }

    public void delete(Patient patient) {
        patientRepository.delete(patient);
    }

    public void save(Patient patient) {
        if (patient == null) {
            LOGGER.log(Level.SEVERE,
                    "Patient is null. Are you sure you have connected your form to the application?");
            return;
        }
        patientRepository.save(patient);
    }

}
