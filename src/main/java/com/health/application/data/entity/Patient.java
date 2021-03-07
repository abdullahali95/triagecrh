package com.health.application.data.entity;

import com.health.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Patient extends AbstractEntity implements Cloneable{

    @NotNull
    private int hospId;
    private double nhsId;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    private LocalDate dob;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    private List<Admission> admissions = new LinkedList<>();

    public List<Admission> getAdmissions() {
        return admissions;
    }

    public void setAdmissions(List<Admission> admissions) {
        this.admissions = admissions;
    }

    public int getHospId() { return hospId; }

    public void setHospId(int hospId) { this.hospId = hospId; }

    public double getNhsId() {
        return nhsId;
    }

    public void setNhsId(double nhsId) {
        this.nhsId = nhsId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
