package com.health.application.data.service;

import com.health.application.data.entity.Ward;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class WardService {

    private static final Logger LOGGER = Logger.getLogger(WardService.class.getName());
    private WardRepository repository;

    public WardService (WardRepository repository) {
        this.repository = repository;
    }

    public List<Ward> findAll() {
        return repository.findAll();
    }

    public long count() {
        return repository.count();
    }

    public void delete(Ward ward) {
        repository.delete(ward);
    }

    public void save(Ward ward) {
        if (ward == null) {
            LOGGER.log(Level.SEVERE,
                    "Ward is null. Are you sure you have connected your form to the application?");
            return;
        }
        repository.save(ward);
    }

    public Ward findEMU() {
        return repository.findEmu();
    }
}