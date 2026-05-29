package com.healthcare.platform.repositories;

import com.healthcare.platform.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Layer interacting directly with the MySQL 'patients' and 'users' tables.
 * Extends JpaRepository to leverage standard CRUD operations and custom JPQL/SQL executions.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    /**
     * Finder method to locate a patient profile using their core account email.
     * Leverages JPA Object-Relational Mapping to traverse the User entity relationship.
     * * @param email The target account email string
     * @return An Optional containing the Patient if found
     */
    Optional<Patient> findByUserEmail(String email);

    /**
     * Custom Query: Retrieves all patients with a specific blood type grouping.
     * Mapped to specific Admin medical-demographic analytical features.
     * * @param bloodType Enum-matched string like 'A+', 'O-', etc.
     * @return List of matching Patient entities
     */
    List<Patient> findByBloodType(String bloodType);

    /**
     * Native Query Example: Locates a patient profile by cross-referencing their 
     * Emergency Contact string attribute using a fuzzy search.
     * * @param contactKeyword Partial name or sequence to match
     * @return List of matching Patient entities matching the relational criteria
     */
    @Query(value = "SELECT p.* FROM patients p JOIN users u ON p.patient_id = u.user_id " +
                   "WHERE p.emergency_contact LIKE %:keyword%", nativeQuery = true)
    List<Patient> searchByEmergencyContactKeyword(@Param("keyword") String contactKeyword);
}
