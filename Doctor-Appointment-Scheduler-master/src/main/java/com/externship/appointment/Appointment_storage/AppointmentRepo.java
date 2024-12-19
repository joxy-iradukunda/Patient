package com.externship.appointment.Appointment_storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    
    @Query("SELECT a FROM Appointment a WHERE a.email = :email ORDER BY a.date DESC, a.time DESC")
    List<Appointment> findAllByEmailOrderByDateTimeDesc(@Param("email") String email);
    
    @Query("SELECT a FROM Appointment a WHERE a.email = :email")
    List<Appointment> findByEmail(@Param("email") String email);
    
    @Query("SELECT a FROM Appointment a WHERE a.docId = :docId")
    List<Appointment> findByDocId(@Param("docId") String docId);
}
