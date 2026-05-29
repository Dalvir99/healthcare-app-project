package com.healthcare.platform.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise Model representing the Doctor entity within the healthcare system.
 * Maps directly to application authentication systems and database layers.
 */
public class Doctor {
    
    // Core attributes inherited conceptually from User entity
    private int doctorId;
    private String firstName;
    private String lastName;
    private String email;
    
    // Doctor-specific credentialing and clinical attributes
    private String specialization;
    private String licenseNumber;
    private int yearsOfExperience;
    private boolean isVerified;
    private LocalDateTime createdAt;
    
    // Track schedules and relationships
    private List<String> availableTimeSlots;

    /**
     * Default Constructor
     */
    public Doctor() {
        this.availableTimeSlots = new ArrayList<>();
        this.isVerified = false; // Default system state requires admin approval
    }

    /**
     * Parameterized Constructor for application instantiation and DB mapping
     */
    public Doctor(int doctorId, String firstName, String lastName, String email, 
                  String specialization, String licenseNumber, int yearsOfExperience) {
        this.doctorId = doctorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.yearsOfExperience = yearsOfExperience;
        this.isVerified = false;
        this.availableTimeSlots = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    // ==========================================
    // Business Logic Methods (Doctor Workflow)
    // ==========================================

    /**
     * Core Requirement: Allows the doctor to append new clinical availability blocks
     * @param dateTimeSlot Format "YYYY-MM-DD HH:MM"
     */
    public void addAvailabilitySlot(String dateTimeSlot) {
        if (dateTimeSlot != null && !this.availableTimeSlots.contains(dateTimeSlot)) {
            this.availableTimeSlots.add(dateTimeSlot);
        }
    }

    /**
     * Core Requirement: Used by Admin workflows to verify credential validity
     * @param verifiedStatus status from verification audit
     */
    public void setVerificationStatus(boolean verifiedStatus) {
        this.isVerified = verifiedStatus;
    }

    // ==========================================
    // Getters and Setters (Encapsulation)
    // ==========================================

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return "Dr. " + this.firstName + " " + this.lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public List<String> getAvailableTimeSlots() {
        return new ArrayList<>(this.availableTimeSlots); // Returns defensive copy
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + doctorId +
                ", name='" + getFullName() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", license='" + licenseNumber + '\'' +
                ", verified=" + isVerified +
                '}';
    }
}
