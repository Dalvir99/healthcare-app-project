package com.healthcare.platform.controllers;

import com.healthcare.platform.models.Prescription;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller that exposes and manages endpoints for Prescription-related operations.
 * Maps incoming HTTP requests to corresponding clinical backend functionality.
 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    // Mock internal data store representing our database interactions
    private final List<Prescription> prescriptionDatabase = new ArrayList<>();

    /**
     * Endpoint: Issue a new prescription for a patient.
     * Mapped to the Doctor Workflow (Core User Story).
     */
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        if (prescription == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        // Auto-increment ID simulation
        int newId = prescriptionDatabase.size() + 1;
        prescription.setPrescriptionId(newId);
        
        prescriptionDatabase.add(prescription);
        return new ResponseEntity<>(prescription, HttpStatus.CREATED);
    }

    /**
     * Endpoint: Retrieve a specific prescription by its unique Identifier.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable("id") int id) {
        Optional<Prescription> prescription = prescriptionDatabase.stream()
                .filter(p -> p.getPrescriptionId() == id)
                .findFirst();

        return prescription.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint: Retrieve all prescriptions tied to an Appointment.
     */
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Prescription> getPrescriptionByAppointment(@PathVariable("appointmentId") int appointmentId) {
        Optional<Prescription> prescription = prescriptionDatabase.stream()
                .filter(p -> p.getAppointmentId() == appointmentId)
                .findFirst();

        return prescription.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
