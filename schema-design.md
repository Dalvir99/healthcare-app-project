# Database Schema Design

This document outlines the MySQL relational database schema designed to support the Doctor, Patient, and Admin user workflows.

## Entity Relationship Diagram (Conceptual Layout)
* **Users** (One-to-One) -> **Patients** / **Doctors**
* **Doctors** (One-to-Many) -> **Availability**
* **Patients & Doctors** (Many-to-Many) -> **Appointments**
* **Appointments** (One-to-One) -> **Prescriptions**

---

## MySQL Table DDL (Data Definition Language)

### 1. Users Table
Stores core authentication and profile data for all system users. Admins exist strictly in this table with administrative roles.

```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('admin', 'doctor', 'patient') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE patients (
    patient_id INT PRIMARY KEY,
    date_of_birth DATE NOT NULL,
    gender ENUM('male', 'female', 'other') NOT NULL,
    blood_type ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    emergency_contact VARCHAR(100),
    FOREIGN KEY (patient_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE doctors (
    doctor_id INT PRIMARY KEY,
    specialization VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    years_of_experience INT DEFAULT 0,
    is_verified BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (doctor_id) REFERENCES users(user_id) ON DELETE CASCADE
);
