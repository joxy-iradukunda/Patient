package com.externship.appointment.Appointment_storage;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "appointments")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id")
    private Long appId;

    @NotBlank(message = "Patient email is required")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Doctor ID is required")
    @Column(name = "doc_id")
    private String docId;

    @NotBlank(message = "Doctor name is required")
    @Column(name = "doc_name")
    private String docName;

    @NotBlank(message = "Doctor specialization is required")
    @Column(name = "doc_special")
    private String docSpecial;

    @Column(name = "status")
    private String status = "Active";

    @NotNull(message = "Appointment date is required")
    @Column(name = "date")
    private Date date;

    @Column(name = "symptoms")
    private String symptoms;
    
    @Column(name = "time")
    private Time time;

    public Appointment() {
    }

    public Appointment(String email, String docId, String docName, String docSpecial, Date date, Time time,
            String symptoms) {
        this.email = email;
        this.docId = docId;
        this.docName = docName;
        this.docSpecial = docSpecial;
        this.date = date;
        this.time = time;
        this.symptoms = symptoms;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocSpecial() {
        return docSpecial;
    }

    public void setDocSpecial(String docSpecial) {
        this.docSpecial = docSpecial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appId=" + appId +
                ", email='" + email + '\'' +
                ", docId='" + docId + '\'' +
                ", docName='" + docName + '\'' +
                ", docSpecial='" + docSpecial + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", symptoms='" + symptoms + '\'' +
                ", time=" + time +
                '}';
    }
}
