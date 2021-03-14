package com.health.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.health.application.data.AbstractEntity;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Admission extends AbstractEntity {

    @NotNull
    @PastOrPresent(message = "Please enter a valid date of admission")
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

    public enum ClerkedBy {
        None, Reg, IMT, SHO, F1, PA, ACP, MedStudent
    };
    @Enumerated(EnumType.STRING)
    @NotNull
    private Admission.ClerkedBy clerkedBy;
    private boolean postTaken;
    private String presentingComplaint;
    private Integer news;
    private boolean dischargeable;
    private boolean urgentClerk;
    private int frailtyScore;

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
    public ClerkedBy getClerkedBy() {return clerkedBy;}
    public void setClerkedBy(ClerkedBy clerkedBy) {
        this.clerkedBy = clerkedBy;
    }
    public boolean isDischargeable() {return dischargeable;}
    public void setDischargeable(boolean dischargeable) {this.dischargeable = dischargeable;}
    public boolean isUrgentClerk() {return urgentClerk;}
    public void setUrgentClerk(boolean urgentClerk) {this.urgentClerk = urgentClerk;}
    public int getFrailtyScore() {return frailtyScore;}
    public void setFrailtyScore(int frailtyScore) {this.frailtyScore = frailtyScore;}

}
