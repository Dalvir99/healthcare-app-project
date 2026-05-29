package com.healthcare.platform.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Enterprise Model representing the Appointment entity within the healthcare system.
 * Manages the relationship and scheduling state between a Patient and a Doctor.
 */
public class Appointment {

    // Unique Identifier
    private int appointmentId;

    // Foreign Key References (Maps to User IDs)
    private int patientId;
    private int doctorId;

    // Schedule Specifics
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    
    // Core Status Enumeration Mapping
    private Status status;
    private String symptomsDescription;
    private LocalDateTime createdAt;

    /**
     * Enum defining the strict workflow states of an appointment.
     */
    public enum Status {
        PENDING,
        CONFIRMED,
        COMPLETED,
        CANCELLED
    }

    /**
     * Default Constructor
     */
    public Appointment() {
        this.status = Status.PENDING; // System default state upon booking request
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Parameterized Constructor for application instantiation and database mapping
     */
    public Appointment(int appointmentId, int patientId, int doctorId, 
                       LocalDate appointmentDate, LocalTime appointmentTime, 
                       String symptomsDescription) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.symptomsDescription = symptomsDescription;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    // ==========================================
    // Business Logic Methods (Lifecycle Management)
    // ==========================================

    /**
     * Updates appointment status to Confirmed. Used by Doctor or Admin workflows.
     */
    public void confirmAppointment() {
        if (this.status == Status.PENDING) {
            this.status = Status.CONFIRMED;
        } else {
            throw new IllegalStateException("Appointment cannot be confirmed from its current state: " + this.status);
        }
    }

    /**
     * Cancels the appointment session. Accessible by Patient, Doctor, or Admin.
     */
    public void cancelAppointment() {
        if (this.status != Status.COMPLETED) {
            this.status = Status.CANCELLED;
        } else {
            throw new IllegalStateException("Completed appointments cannot be cancelled.");
        }
    }

    /**
     * Marks the session as fulfilled. Used by Doctor upon submitting final notes/prescriptions.
     */
    public void completeAppointment() {
        if (this.status == Status.CONFIRMED) {
            this.status = Status.COMPLETED;
        } else {
            throw new IllegalStateException("Only confirmed appointments can be marked as completed.");
        }
    }

    // ==========================================
    // Getters and Setters (Encapsulation)
    // ==========================================

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSymptomsDescription() {
        return symptomsDescription;
    }

    public void setSymptomsDescription(String symptomsDescription) {
        this.symptomsDescription = symptomsDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", date=" + appointmentDate +
                ", time=" + appointmentTime +
                ", status=" + status +
                '}';
    }
}
