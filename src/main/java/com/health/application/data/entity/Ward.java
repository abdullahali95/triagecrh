package com.health.application.data.entity;

import com.health.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Ward extends AbstractEntity {

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 30, message
            = "Please enter a valid Ward name")
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
