package com.healthcare.platform.services;

import com.healthcare.platform.models.Doctor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer responsible for handling the core business logic for Doctors.
 * Intersects incoming API inputs with backend database access configurations.
 */
@Service
public class DoctorService {

    // Mock internal data store representing our database interactions
    private final List<Doctor> doctorDatabase = new ArrayList<>();

    /**
     * Constructor initializing dummy data records for profile service routing validation.
     */
    public DoctorService() {
        doctorDatabase.add(new Doctor(1, "Jane", "Foster", "jane.foster@clinic.com", "Cardiology", "LIC-99823", 12));
        doctorDatabase.add(new Doctor(2, "John", "Watson", "john.watson@clinic.com", "General Medicine", "LIC-44102", 8));
        
        // Mark first doctor as verified out of the gate
        doctorDatabase.get(0).setVerificationStatus(true);
    }

    /**
     * Business Rule: Retrieves all verified doctors currently active in the platform directory.
     * Mapped to the Patient's workflow story for searching medical providers safely.
     * @return List of verified Doctor models
     */
    public List<Doctor> getVerifiedDoctors() {
        return doctorDatabase.stream()
                .filter(Doctor::isVerified)
                .collect(Collectors.toList());
    }

    /**
     * Business Rule: Exposes an explicit lookup tool searching across specialty fields.
     * @param specialization The focus medical field string keyword
     * @return List of matching Doctor results
     */
    public List<Doctor> getDoctorsBySpecialty(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            return getVerifiedDoctors();
        }
        return doctorDatabase.stream()
                .filter(Doctor::isVerified)
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Business Rule: Handles credential auditing verification configurations.
     * Mapped to the Admin workflow story for licensing oversight actions.
     * @param doctorId System ID of the profile target
     * @param verifiedStatus True or False assessment values
     * @return The updated Doctor context object
     */
    public Doctor updateDoctorVerification(int doctorId, boolean verifiedStatus) {
        Doctor doctor = doctorDatabase.stream()
                .filter(d -> d.getDoctorId() == doctorId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile record not found with ID: " + doctorId));
                
        doctor.setVerificationStatus(verifiedStatus);
        return doctor;
    }

    /**
     * Business Rule: Allows doctors to submit dynamic time windows into scheduling pools.
     * Mapped directly to the Doctor's core availability workflow story.
     */
    public void addDoctorAvailability(int doctorId, String dateTimeSlot) {
        Doctor doctor = doctorDatabase.stream()
                .filter(d -> d.getDoctorId() == doctorId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile record not found with ID: " + doctorId));
                
        doctor.addAvailabilitySlot(dateTimeSlot);
    }
}
