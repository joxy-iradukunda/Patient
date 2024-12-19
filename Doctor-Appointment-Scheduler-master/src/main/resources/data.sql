-- Insert test data
insert into person (email, password, name, age, gender) 
values ('alineuwineza123@gmail.com', 'test123', 'Aline Uwineza', '25', 'Female');

insert into doctors (email, password, name, specialization, experience, qualification)
values ('doctor1@test.com', 'test123', 'Dr. John Smith', 'Cardiology', '10 years', 'MD, FACC');

insert into appointments (email, doc_id, doc_name, doc_special, status, date, time, symptoms)
values ('alineuwineza123@gmail.com', 'doctor1@test.com', 'Dr. John Smith', 'Cardiology', 'Active', '2024-12-20', '10:00 AM', 'Regular checkup');
