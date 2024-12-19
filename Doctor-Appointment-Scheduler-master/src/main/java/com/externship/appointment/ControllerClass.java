package com.externship.appointment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.externship.appointment.Appointment_storage.Appointment;
import com.externship.appointment.Appointment_storage.AppointmentRepo;
import com.externship.appointment.Doctor_storage.Doctor;
import com.externship.appointment.Doctor_storage.DoctorRepo;
import com.externship.appointment.Person_storage.Person;
import com.externship.appointment.Person_storage.PersonRepo;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class ControllerClass {
    
    @Autowired
    private DoctorRepo docRepo;
    
    @Autowired
    private PersonRepo personRepo;
    
    @Autowired
    private AppointmentRepo appRepo;

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @GetMapping("/registerdoc")
    public String registerdoc() {
        return "registerdoc";
    }

    @GetMapping("/")
    public String home() {
        return "start";
    }

    @GetMapping("/patlog")
    public String patlog() {
        return "index";
    }

    @GetMapping("/doclog")
    public String doclog() {
        return "doclog";
    }
    
    @PostMapping("/registered")
    public ResponseEntity<?> registered(@RequestBody Person person) {
        try {
            System.out.println("Registering new user: " + person.getEmail());
            
            // Validate required fields
            if (person.getEmail() == null || person.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (person.getPassword() == null || person.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            if (person.getName() == null || person.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required");
            }
            if (person.getAge() == null || !person.getAge().matches("^\\d{1,3}$")) {
                return ResponseEntity.badRequest().body("Valid age is required");
            }
            if (person.getGender() == null || !person.getGender().toLowerCase().matches("^(male|female|other)$")) {
                return ResponseEntity.badRequest().body("Gender must be male, female, or other");
            }

            // Check if email already exists
            if (personRepo.existsById(person.getEmail())) {
                System.out.println("User already exists: " + person.getEmail());
                return ResponseEntity.badRequest().body("Email already exists");
            }

            // Clean and normalize the data
            person.setEmail(person.getEmail().toLowerCase().trim());
            person.setName(person.getName().trim());
            person.setGender(person.getGender().toLowerCase().trim());

            // Save the person
            Person savedPerson = personRepo.save(person);
            System.out.println("User registered successfully: " + savedPerson.getEmail());
            
            return ResponseEntity.ok().body("Registration successful");
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/registereddoc")
    public ResponseEntity<?> registereddoc(@RequestBody Doctor doctor) {
        try {
            if (doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (doctor.getPassword() == null || doctor.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required");
            }
            if (doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Specialization is required");
            }
            if (doctor.getExperience() == null || doctor.getExperience().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Experience is required");
            }
            if (doctor.getQualification() == null || doctor.getQualification().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Qualification is required");
            }

            if (docRepo.existsById(doctor.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            Doctor savedDoctor = docRepo.save(doctor);
            System.out.println("Doctor saved successfully: " + savedDoctor);
            return ResponseEntity.ok("Registration successful");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Person person, HttpSession session) {
        try {
            System.out.println("Session ID: " + session.getId());
            System.out.println("Authenticating user: " + person.getEmail());
            
            if (person.getEmail() == null || person.getEmail().trim().isEmpty()) {
                System.out.println("Email is missing");
                return ResponseEntity.badRequest().body("Email is required");
            }
            
            if (person.getPassword() == null || person.getPassword().trim().isEmpty()) {
                System.out.println("Password is missing");
                return ResponseEntity.badRequest().body("Password is required");
            }

            Optional<Person> existingPerson = personRepo.findById(person.getEmail());
            System.out.println("Found user in database: " + existingPerson.isPresent());
            
            if (existingPerson.isPresent()) {
                if (existingPerson.get().getPassword().equals(person.getPassword())) {
                    System.out.println("User authenticated successfully: " + person.getEmail());
                    session.setAttribute("person", person.getEmail());
                    System.out.println("Session attribute set: " + session.getAttribute("person"));
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("email", person.getEmail());
                    response.put("name", existingPerson.get().getName());
                    response.put("sessionId", session.getId());
                    
                    return ResponseEntity.ok(response);
                } else {
                    System.out.println("Password mismatch for user: " + person.getEmail());
                }
            } else {
                System.out.println("User not found: " + person.getEmail());
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/authenticatedoc")
    public ResponseEntity<?> authenticatedoc(@RequestBody Doctor doctor, HttpSession session) {
        try {
            Optional<Doctor> existingDoctor = docRepo.findById(doctor.getEmail());
            if (existingDoctor.isPresent() && existingDoctor.get().getPassword().equals(doctor.getPassword())) {
                session.setAttribute("doctor", doctor.getEmail());
                return ResponseEntity.ok(existingDoctor.get());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/fail_login")
    public String fail_login() {
        return "fail_login";
    }

    @GetMapping("/docdetails")
    public ResponseEntity<?> getDocDetails() {
        try {
            List<Doctor> doctors = docRepo.findAll();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch doctor details: " + e.getMessage());
        }
    }

    @GetMapping("/userdetails")
    public ResponseEntity<?> getUserDetails(HttpSession session) {
        try {
            String userEmail = (String) session.getAttribute("person");
            System.out.println("Session ID: " + session.getId());
            System.out.println("Getting appointments for user: " + userEmail);
            
            if (userEmail == null) {
                System.out.println("User not logged in");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
            }

            List<Appointment> appointments = appRepo.findAllByEmailOrderByDateTimeDesc(userEmail);
            System.out.println("Found " + appointments.size() + " appointments for user: " + userEmail);
            
            Map<String, Object> response = new HashMap<>();
            response.put("email", userEmail);
            response.put("appointments", appointments);
            
            System.out.println("Returning response with " + appointments.size() + " appointments");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error getting user details: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch user details: " + e.getMessage());
        }
    }

    @PostMapping("/assignment")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment app) {
        try {
            System.out.println("Booking appointment for user: " + app.getEmail());
            
            if (app.getEmail() == null || app.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Patient email is required");
            }
            if (app.getDocId() == null || app.getDocId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Doctor ID is required");
            }
            if (app.getDocName() == null || app.getDocName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Doctor name is required");
            }
            if (app.getDocSpecial() == null || app.getDocSpecial().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Doctor specialization is required");
            }
            if (app.getDate() == null || app.getDate().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Appointment date is required");
            }

            Optional<Doctor> doctor = docRepo.findById(app.getDocId());
            if (!doctor.isPresent()) {
                System.out.println("Doctor not found: " + app.getDocId());
                return ResponseEntity.badRequest().body("Selected doctor does not exist");
            }

            app.setStatus("Active");
            Appointment savedAppointment = appRepo.save(app);
            System.out.println("Appointment saved successfully with ID: " + savedAppointment.getAppId());

            return ResponseEntity.ok().body(Map.of(
                "message", "Appointment booked successfully",
                "appointmentId", savedAppointment.getAppId()
            ));
        } catch (Exception e) {
            System.err.println("Error booking appointment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to book appointment: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelAppointment(@RequestBody Map<String, Integer> request) {
        try {
            Integer appId = request.get("appId");
            if (appId == null) {
                return ResponseEntity.badRequest().body("Appointment ID is required");
            }

            Optional<Appointment> appointment = appRepo.findById(appId);

            if (appointment.isPresent()) {
                Appointment app = appointment.get();
                app.setStatus("Cancelled");
                appRepo.save(app);
                return ResponseEntity.ok().body("Appointment cancelled successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to cancel appointment: " + e.getMessage());
        }
    }

    @GetMapping("/display")
    public ResponseEntity<?> display(HttpSession session) {
        try {
            String userEmail = (String) session.getAttribute("person");
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
            }

            List<Appointment> appointments = appRepo.findByEmail(userEmail);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch appointments: " + e.getMessage());
        }
    }

    @GetMapping("/patientlist")
    public ResponseEntity<?> getPatientList(HttpSession session) {
        try {
            String doctorEmail = (String) session.getAttribute("doctor");
            if (doctorEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
            }

            List<Appointment> appointments = appRepo.findByDocId(doctorEmail);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch patient list: " + e.getMessage());
        }
    }

    @PostMapping("/updatestatus")
    public ResponseEntity<?> updateAppointment(@RequestBody Map<String, Integer> request) {
        try {
            Integer appId = request.get("appId");
            String status = request.get("status").toString();

            if (appId == null || status == null) {
                return ResponseEntity.badRequest().body("Appointment ID and status are required");
            }

            Optional<Appointment> appointment = appRepo.findById(appId);

            if (appointment.isPresent()) {
                Appointment app = appointment.get();
                app.setStatus(status);
                appRepo.save(app);
                return ResponseEntity.ok().body("Appointment status updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update appointment status: " + e.getMessage());
        }
    }
}
