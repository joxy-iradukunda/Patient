package com.externship.appointment.Doctor_storage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Specialization is required")
    @Column(nullable = false)
    private String specialization;

    @Pattern(regexp = "^\\d{1,3}$", message = "Age must be a valid number between 1 and 120")
    @Column(nullable = false)
    private String age;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(male|female|other)$", message = "Gender must be male, female, or other")
    @Column(nullable = false)
    private String gender;

    @NotBlank(message = "Qualification is required")
    @Column(nullable = false)
    private String qualification;

    public Doctor() {
    }

    public Doctor(String email, String password, String name, String specialization, String age, String gender,
            String qualification) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.specialization = specialization;
        this.age = age;
        this.gender = gender;
        this.qualification = qualification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.toLowerCase() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender != null ? gender.toLowerCase() : null;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", qualification='" + qualification + '\'' +
                '}';
    }
}
