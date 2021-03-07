package com.health.application.data.entity;

import com.health.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Ward extends AbstractEntity {

    @NotNull
    @NotEmpty
    private String wardName;

    @OneToMany(mappedBy = "ward", fetch = FetchType.EAGER)
    private List<Admission> admissions = new LinkedList<>();

    public List<Admission> getAdmissions() {
        return admissions;
    }

    public void setAdmissions(List<Admission> admissions) {
        this.admissions = admissions;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }
}
