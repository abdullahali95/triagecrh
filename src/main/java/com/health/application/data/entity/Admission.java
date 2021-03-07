package com.health.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.health.application.data.AbstractEntity;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Admission extends AbstractEntity {

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    private boolean clerked;
    private boolean postTaken;
    private String presentingComplaint;
    private Integer news;

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public Ward getWard() {
        return ward;
    }
    public void setWard(Ward ward) {
        this.ward = ward;
    }
    public boolean isClerked() {
        return clerked;
    }
    public void setClerked(boolean clerked) {
        this.clerked = clerked;
    }
    public boolean isPostTaken() {
        return postTaken;
    }
    public void setPostTaken(boolean postTaken) {
        this.postTaken = postTaken;
    }
    public String getPresentingComplaint() {
        return presentingComplaint;
    }
    public void setPresentingComplaint(String presentingComplaint) {
        this.presentingComplaint = presentingComplaint;
    }
    public Integer getNews() {
        return news;
    }
    public void setNews(Integer news) {
        this.news = news;
    }

}
