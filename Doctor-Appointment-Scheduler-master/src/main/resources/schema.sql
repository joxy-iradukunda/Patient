-- Drop tables if they exist
DROP TABLE IF EXISTS appointments CASCADE;
DROP TABLE IF EXISTS doctors CASCADE;
DROP TABLE IF EXISTS person CASCADE;

-- Create person table
CREATE TABLE IF NOT EXISTS person (
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    age VARCHAR(50) NOT NULL,
    gender VARCHAR(50) NOT NULL
);

-- Create doctors table
CREATE TABLE IF NOT EXISTS doctors (
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    age VARCHAR(50) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    qualification VARCHAR(255) NOT NULL
);

-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
    app_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    doc_id VARCHAR(255) NOT NULL,
    doc_name VARCHAR(255) NOT NULL,
    doc_special VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'Active',
    date DATE NOT NULL,
    symptoms VARCHAR(500),
    time TIME,
    FOREIGN KEY (doc_id) REFERENCES doctors(email),
    FOREIGN KEY (email) REFERENCES person(email)
);
