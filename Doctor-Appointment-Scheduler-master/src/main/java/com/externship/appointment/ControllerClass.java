package com.externship.appointment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    private PersonRepo personRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;

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
    public ResponseEntity<?> addPerson(@RequestBody Person person) {
        try {
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

            if (personRepo.existsById(person.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            person.setEmail(person.getEmail().toLowerCase().trim());
            person.setName(person.getName().trim());
            person.setGender(person.getGender().toLowerCase().trim());

            Person savedPerson = personRepo.save(person);
            return ResponseEntity.ok(savedPerson);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/registereddoc")
    public ResponseEntity<?> addDoctor(@RequestBody Doctor doctor) {
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
            if (doctor.getQualification() == null || doctor.getQualification().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Qualification is required");
            }

            if (doctorRepo.existsById(doctor.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            Doctor savedDoctor = doctorRepo.save(doctor);
            return ResponseEntity.ok(savedDoctor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> validateUser(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        Person person = personRepo.findByEmail(email);
        if (person != null && person.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("userEmail", person.getEmail());
            return ResponseEntity.ok(person);
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @PostMapping("/authenticatedoc")
    public ResponseEntity<?> validateDoctor(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        Doctor doctor = doctorRepo.findByEmail(email);
        if (doctor != null && doctor.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("doctor", doctor.getEmail());
            return ResponseEntity.ok(doctor);
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @GetMapping("/fail_login")
    public String fail_login() {
        return "fail_login";
    }

    @GetMapping("/docdetails")
    public ResponseEntity<?> getDocDetails() {
        try {
            List<Doctor> doctors = doctorRepo.findAll();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch doctor details: " + e.getMessage());
        }
    }

    @GetMapping("/userdetails")
    public ResponseEntity<?> getUserDetails(HttpSession session) {
        try {
            String userEmail = (String) session.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.badRequest().body("User not logged in");
            }

            List<Appointment> appointments = appointmentRepo.findByEmail(userEmail);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch user details: " + e.getMessage());
        }
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userEmail") == null) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }

            String userEmail = (String) session.getAttribute("userEmail");
            if (!userEmail.equals(appointment.getEmail())) {
                return ResponseEntity.badRequest().body("Unauthorized to book appointment for different user");
            }

            if (appointment.getEmail() == null || appointment.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Patient email is required");
            }
            if (appointment.getDocId() == null || appointment.getDocId().isEmpty()) {
                return ResponseEntity.badRequest().body("Doctor ID is required");
            }
            if (appointment.getDate() == null) {
                return ResponseEntity.badRequest().body("Appointment date is required");
            }

            Optional<Doctor> doctor = doctorRepo.findById(appointment.getDocId());
            if (!doctor.isPresent()) {
                return ResponseEntity.badRequest().body("Selected doctor does not exist");
            }

            appointment.setStatus("Active");
            Appointment savedAppointment = appointmentRepo.save(appointment);
            return ResponseEntity.ok(savedAppointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage());
        }
    }

    @PostMapping("/cancel-appointment")
    public ResponseEntity<?> cancelAppointment(@RequestBody Map<String, Long> request, HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession(false);
            if (session == null) {
                return ResponseEntity.badRequest().body("No active session");
            }

            Long appId = request.get("appId");
            if (appId == null) {
                return ResponseEntity.badRequest().body("Appointment ID is required");
            }

            Optional<Appointment> appointment = appointmentRepo.findById(appId);

            if (appointment.isPresent()) {
                Appointment app = appointment.get();
                String userEmail = (String) session.getAttribute("userEmail");
                String doctorEmail = (String) session.getAttribute("doctor");

                if (userEmail != null && app.getEmail().equals(userEmail) ||
                        doctorEmail != null && app.getDocId().equals(doctorEmail)) {
                    app.setStatus("Cancelled");
                    appointmentRepo.save(app);
                    return ResponseEntity.ok().body("Appointment cancelled successfully");
                }

                return ResponseEntity.badRequest().body("Unauthorized to cancel this appointment");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cancellation failed: " + e.getMessage());
        }
    }

    @GetMapping("/display")
    public ResponseEntity<?> display(HttpSession session) {
        try {
            String userEmail = (String) session.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.badRequest().body("User not logged in");
            }

            List<Appointment> appointments = appointmentRepo.findByEmail(userEmail);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch appointments: " + e.getMessage());
        }
    }

    @GetMapping("/patientlist")
    public ResponseEntity<?> getPatientList(HttpSession session) {
        try {
            String doctorEmail = (String) session.getAttribute("doctor");
            if (doctorEmail == null) {
                return ResponseEntity.badRequest().body("Doctor not logged in");
            }

            List<Appointment> appointments = appointmentRepo.findByDocId(doctorEmail);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch patient list: " + e.getMessage());
        }
    }

    @PostMapping("/update-appointment")
    public ResponseEntity<?> updateAppointment(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession(false);
            if (session == null) {
                return ResponseEntity.badRequest().body("No active session");
            }

            Long appId = Long.valueOf(request.get("appId").toString());
            String status = (String) request.get("status");

            if (appId == null || status == null) {
                return ResponseEntity.badRequest().body("Appointment ID and status are required");
            }

            Optional<Appointment> appointment = appointmentRepo.findById(appId);

            if (appointment.isPresent()) {
                Appointment app = appointment.get();
                String userEmail = (String) session.getAttribute("userEmail");
                String doctorEmail = (String) session.getAttribute("doctor");

                if (userEmail != null && app.getEmail().equals(userEmail) ||
                        doctorEmail != null && app.getDocId().equals(doctorEmail)) {
                    app.setStatus(status);
                    appointmentRepo.save(app);
                    return ResponseEntity.ok().body("Appointment status updated successfully");
                }

                return ResponseEntity.badRequest().body("Unauthorized to update this appointment");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update appointment status: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().body("Logged out successfully");
    }
}
