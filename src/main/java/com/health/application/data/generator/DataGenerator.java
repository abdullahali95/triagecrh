//package com.health.application.data.generator;
//
//import com.health.application.data.entity.Patient;
//import com.health.application.data.entity.Ward;
//import com.vaadin.flow.spring.annotation.SpringComponent;
//
//import com.health.application.data.service.AdmissionRepository;
//import com.health.application.data.entity.Admission;
//
//import java.time.LocalDateTime;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.vaadin.artur.exampledata.DataType;
//import org.vaadin.artur.exampledata.ExampleDataGenerator;
//
//@SpringComponent
//public class DataGenerator {
//
//    private Patient Patient;
//
//    @Bean
//    public CommandLineRunner loadData(AdmissionRepository admissionRepository) {
//        return args -> {
//            Logger logger = LoggerFactory.getLogger(getClass());
//            if (admissionRepository.count() != 0L) {
//                logger.info("Using existing database");
//                return;
//            }
//            int seed = 123;
//
//            logger.info("Generating demo data");
//
//            logger.info("... generating 100 Admission entities...");
//            Patient pt = new Patient();
//            Ward ward = new Ward();
//            ExampleDataGenerator<Patient> patientRepositoryGenerator = new ExampleDataGenerator<>(Patient.class,
//                    LocalDateTime.of(2021, 3, 5, 0, 0, 0));
//
//            ExampleDataGenerator<Admission> admissionRepositoryGenerator = new ExampleDataGenerator<>(Admission.class,
//                    LocalDateTime.of(2021, 3, 5, 0, 0, 0));
//            admissionRepositoryGenerator.setData(Admission::setId, DataType.ID);
//            admissionRepositoryGenerator.setData(Admission::setDate, DataType.DATE_OF_BIRTH);
//            admissionRepositoryGenerator.setData(Admission::setTime, DataType.TIME_RANDOM);
//            admissionRepositoryGenerator.setData(Admission::setClerked, DataType.BOOLEAN_50_50);
//            admissionRepositoryGenerator.setData(Admission::setPostTaken, DataType.BOOLEAN_50_50);
//            admissionRepositoryGenerator.setData(Admission::setPresentingComplaint, DataType.WORD);
//            admissionRepositoryGenerator.setData(Admission::setNews, DataType.NUMBER_UP_TO_10);
//            admissionRepositoryGenerator.setData(Admission::setNews, DataType.NUMBER_UP_TO_10);
//            admissionRepository.saveAll(admissionRepositoryGenerator.create(100, seed));
//
//            logger.info("Generated demo data");
//        };
//    }
//
//}