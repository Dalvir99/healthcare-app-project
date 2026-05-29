package com.healthcare.platform.controllers;

import com.healthcare.platform.models.Doctor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller that exposes and manages endpoints for Doctor-related operations.
 * Coordinates HTTP requests between front-end UI clients and the underlying business logic layer.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    // Mock internal data store representing database interactions for this example
    private final List<Doctor> doctorRepository = new ArrayList<>();

    /**
     * Constructor populating baseline mock data for operational routing tests.
     */
    public DoctorController() {
        doctorRepository.add(new Doctor(1, "Jane", "Foster", "jane.foster@clinic.com", "Cardiology", "LIC-99823", 12));
        doctorRepository.add(new Doctor(2, "John", "Watson", "john.watson@clinic.com", "General Medicine", "LIC-44102", 8));
    }

    /**
     * Endpoint: Retrieve list of all registered doctors.
     * Accessible by: Patients (to search) and Admins (to view system directory).
     */
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return new ResponseEntity<>(doctorRepository, HttpStatus.OK);
    }

    /**
     * Endpoint: Retrieve profile details for a specific doctor.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable("id") int id) {
        Optional<Doctor> doctor = doctorRepository.stream()
                .filter(d -> d.getDoctorId() == id)
                .findFirst();

        return doctor.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint: Allows a Doctor to update or append their professional availability window.
     * Dynamic Action mapped to custom Doctor User Story requirements.
     */
    @PostMapping("/{id}/availability")
    public ResponseEntity<String> addAvailability(@PathVariable("id") int id, @RequestParam("slot") String dateTimeSlot) {
        Optional<Doctor> doctor = doctorRepository.stream()
                .filter(d -> d.getDoctorId() == id)
                .findFirst();

        if (doctor.isPresent()) {
            doctor.get().addAvailabilitySlot(dateTimeSlot);
            return new ResponseEntity<>("Availability slot updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Doctor profile not found.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint: Allows an Admin to verify a Doctor's credential parameters.
     * Admin Workflow Requirement.
     */
    @PutMapping("/{id}/verify")
    public ResponseEntity<Doctor> verifyDoctorCredentials(@PathVariable("id") int id, @RequestParam("status") boolean status) {
        Optional<Doctor> doctor = doctorRepository.stream()
                .filter(d -> d.getDoctorId() == id)
                .findFirst();

        if (doctor.isPresent()) {
            doctor.get().setVerificationStatus(status);
            return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

Step-by-Step Submission Instructions:
Create the File: Save this file as DoctorController.java within your project folder architecture. The typical structure follows a standard MVC format (e.g., src/main/java/com/healthcare/platform/controllers/DoctorController.java).

Commit and Push: Commit this file to your public project repository using standard git commands:

Bash
git add DoctorController.java
git commit -m "feat: Add Doctor Controller REST API endpoints"
git push origin main









