DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS person;

CREATE TABLE person (
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255),
    name VARCHAR(255),
    age VARCHAR(50),
    gender VARCHAR(50)
);

CREATE TABLE doctors (
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255),
    name VARCHAR(255),
    specialization VARCHAR(255),
    experience VARCHAR(50),
    qualification VARCHAR(255)
);

CREATE TABLE appointments (
    app_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    doc_id VARCHAR(255) NOT NULL,
    doc_name VARCHAR(255) NOT NULL,
    doc_special VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'Active',
    date VARCHAR(50) NOT NULL,
    symptoms VARCHAR(500),
    time VARCHAR(50),
    FOREIGN KEY (doc_id) REFERENCES doctors(email),
    FOREIGN KEY (email) REFERENCES person(email)
);
