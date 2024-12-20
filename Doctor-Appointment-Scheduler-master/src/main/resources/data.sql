-- Insert test doctors
INSERT INTO doctors (email, password, name, specialization, age, gender, qualification) 
VALUES 
('doctor1@test.com', 'password123', 'Dr. John Smith', 'Cardiology', '45', 'male', 'MD, FACC'),
('doctor2@test.com', 'password123', 'Dr. Sarah Johnson', 'Pediatrics', '38', 'female', 'MD, FAAP');

-- Insert test patients
INSERT INTO person (email, password, name, age, gender)
VALUES 
('patient1@test.com', 'password123', 'James Wilson', '35', 'male'),
('patient2@test.com', 'password123', 'Emily Brown', '28', 'female');

-- Insert test appointments
insert into appointments (email, doc_id, doc_name, doc_special, status, date, time, symptoms)
values ('patient1@test.com', 'doctor1@test.com', 'Dr. John Smith', 'Cardiology', 'Active', '2024-12-20', '10:00 AM', 'Regular checkup');
