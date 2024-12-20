package com.externship.appointment.Person_storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends JpaRepository<Person, String> {
    Person findByEmail(String email);
}
