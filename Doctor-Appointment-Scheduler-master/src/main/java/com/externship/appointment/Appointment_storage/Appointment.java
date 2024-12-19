package com.externship.appointment.Appointment_storage;

import javax.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id")
    private Long appId;

    @Column(nullable = false)
    private String email;

    @Column(name = "doc_id", nullable = false)
    private String docId;

    @Column(name = "doc_name", nullable = false)
    private String docName;

    @Column(name = "doc_special", nullable = false)
    private String docSpecial;

    @Column(nullable = false)
    private String status = "Active";

    @Column(nullable = false)
    private String date;

    @Column
    private String symptoms;

    @Column
    private String time;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
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

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
                ", date='" + date + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
