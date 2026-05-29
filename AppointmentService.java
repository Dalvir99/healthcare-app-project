package com.healthcare.platform.services;

import com.healthcare.platform.models.Appointment;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer responsible for handling the core business logic of Appointments.
 * Ensures data integrity, prevents booking conflicts, and coordinates status updates.
 */
@Service
public class AppointmentService {

    // Mock internal data store representing our database interactions
    private final List<Appointment> appointmentDatabase = new ArrayList<>();

    /**
     * Business Rule: Creates a new appointment after checking for booking double-bookings.
     * * @param patientId The ID of the patient making the booking
     * @param doctorId The ID of the requested doctor
     * @param date The scheduled date
     * @param time The scheduled time slot
     * @param description Brief explanation of symptoms
     * @return The created Appointment entity
     * @throws IllegalStateException If the doctor already has an appointment at that time
     */
    public synchronized Appointment scheduleAppointment(int patientId, int doctorId, 
                                                         LocalDate date, LocalTime time, 
                                                         String description) {
        
        // Check for scheduling conflict: Does this doctor already have a booking at this date/time?
        boolean hasConflict = appointmentDatabase.stream()
                .anyMatch(app -> app.getDoctorId() == doctorId 
                        && app.getAppointmentDate().equals(date) 
                        && app.getAppointmentTime().equals(time)
                        && app.getStatus() != Appointment.Status.CANCELLED);

        if (hasConflict) {
            throw new IllegalStateException("The selected doctor is already booked for this specific date and time slot.");
        }

        // Auto-increment ID simulation
        int newId = appointmentDatabase.size() + 1;
        
        Appointment newAppointment = new Appointment(newId, patientId, doctorId, date, time, description);
        appointmentDatabase.add(newAppointment);
        
        return newAppointment;
    }

    /**
     * Business Rule: Retrieves all appointments scheduled for a specific patient profile.
     */
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentDatabase.stream()
                .filter(app -> app.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    /**
     * Business Rule: Retrieves all active appointments scheduled under a specific doctor.
     */
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointmentDatabase.stream()
                .filter(app -> app.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    /**
     * Business Rule: Confirms a pending appointment booking.
     */
    public Appointment confirmAppointment(int appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        appointment.confirmAppointment();
        return appointment;
    }

    /**
     * Business Rule: Cancels an existing appointment booking.
     */
    public Appointment cancelAppointment(int appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        appointment.cancelAppointment();
        return appointment;
    }

    /**
     * Helper Method: Searches internal storage for an explicit Appointment entry.
     */
    private Appointment findAppointmentById(int appointmentId) {
        return appointmentDatabase.stream()
                .filter(app -> app.getAppointmentId() == appointmentId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Appointment ID record not found: " + appointmentId));
    }
}
