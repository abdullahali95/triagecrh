package com.health.application.data.service;

import com.health.application.data.entity.Admission;

import com.health.application.data.entity.Patient;
import com.health.application.data.entity.Ward;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AdmissionService {
    private static final Logger LOGGER = Logger.getLogger(AdmissionService.class.getName());
    private AdmissionRepository admissionRepository;
    private PatientRepository patientRepository;
    private WardRepository wardRepository;

    public AdmissionService(AdmissionRepository admissionRepository,
                            PatientRepository patientRepository, WardRepository wardRepository) {
        this.admissionRepository = admissionRepository;
        this.patientRepository = patientRepository;
        this.wardRepository = wardRepository;
    }

    public List<Admission> findAll() {
        return admissionRepository.findAll();
    }

    public long count() {
        return admissionRepository.count();
    }

    public void delete(Admission admission) {
        admissionRepository.delete(admission);
    }

    public void save(Admission admission) {
        if (admission == null) {
            LOGGER.log(Level.SEVERE,
                    "Admission is null. Are you sure you have connected your form to the application?");
            return;
        }
        admissionRepository.save(admission);
    }

    /**
     * Gets all the admissions, then gets the patients matching search term
     * If the patients match an admission, the admissions are returned
     * @param stringFilter
     * @return
     */
    public List<Admission> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return admissionRepository.findAll();
        } else {
            List<Admission> allAdmissions = admissionRepository.findAll();
            List<Patient> allPts = patientRepository.search(stringFilter);
            List<Admission> finalList = new ArrayList<Admission>();
            for (Admission a : allAdmissions) {
                if (allPts.contains(a.getPatient())) {
                    finalList.add(a);
                }
            }
            return finalList;
        }
    }

    public List<Admission> findAll(int filter) {
        if (filter == 0) {
            return admissionRepository.findAll();
        } else {
            List<Admission> allAdmissions = admissionRepository.findAll();
            List<Patient> allPts = patientRepository.search(filter);
            List<Admission> finalList = new ArrayList<Admission>();
            for (Admission a : allAdmissions) {
                if (allPts.contains(a.getPatient())) {
                    finalList.add(a);
                }
            }
            return finalList;
        }
    }

    public List<Patient> findAllPts(int filter) {
        if (filter == 0) {
            return null;
        } else {
            List<Patient> allPts = patientRepository.search(filter);
            return allPts;
        }
    }

    public List<Admission>  findAllByWard(Ward ward) {
        if (ward == null) {
            return admissionRepository.findAll();
        } else {
            List<Admission> admissions = admissionRepository.findAll();
            List<Admission> matched = new ArrayList<>();
            for (Admission a : admissions) {
                if (a.getWard().getWardName().equals(ward.getWardName()))  matched.add(a);
            }
            return matched;
        }

    }


    @PostConstruct
    public void populateTestData() {

        if (wardRepository.count() == 0) {
            String[] wardList = {"Devonshire", "Staveley", "Robinson", "Hasland", "EMU", "CDU"};
            for (String w:wardList) {
                Ward ward = new Ward();
                ward.setWardName(w);
                wardRepository.save(ward);
            }
        }
//
//        if (patientRepository.count() == 0) {
//            Patient pt = new Patient();
//            pt.setDob(LocalDate.now());
//            pt.setFirstName("Abdullah");
//            pt.setLastName("Ali");
//            pt.setHospId(382742);
//            pt.setNhsId(4829383399d);
//            patientRepository.save(pt);
//
//            pt = new Patient();
//            pt.setDob(LocalDate.now());
//            pt.setFirstName("Oliver");
//            pt.setLastName("Twist");
//            pt.setHospId(295742);
//            pt.setNhsId(2499992299d);
//            patientRepository.save(pt);
//
//            Patient pt2 = new Patient();
//            pt2.setDob(LocalDate.now());
//            pt2.setFirstName("Nick");
//            pt2.setLastName("James");
//            pt2.setHospId(837502);
//            pt2.setNhsId(8293826633d);
//            patientRepository.save(pt2);
//
//            pt2 = new Patient();
//            pt2.setDob(LocalDate.now());
//            pt2.setFirstName("Nick");
//            pt2.setLastName("James");
//            pt2.setHospId(837502);
//            pt2.setNhsId(8293826633d);
//            patientRepository.save(pt2);
//
//        }
//
//        if (admissionRepository.count() == 0) {
//            List<Patient> patients = patientRepository.findAll();
//            List<Ward> wards = wardRepository.findAll();
//            for (int i=0; i<10; i++) {
//                Admission ad = new Admission();
//                ad.setPatient(patients.get(ThreadLocalRandom.current().nextInt(0, 3)));
//                ad.setClerked(true);
//                ad.setPostTaken(false);
//                ad.setWard(wards.get(ThreadLocalRandom.current().nextInt(0, 5)));
//                ad.setNews(ThreadLocalRandom.current().nextInt(0, 12));
//                ad.setDate(LocalDate.now());
//                ad.setTime(LocalTime.now());
//                admissionRepository.save(ad);
//            }
//        }
    }


}
