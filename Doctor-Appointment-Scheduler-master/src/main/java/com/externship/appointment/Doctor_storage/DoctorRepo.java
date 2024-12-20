package com.externship.appointment.Doctor_storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, String> {
    Doctor findByEmail(String email);
}
