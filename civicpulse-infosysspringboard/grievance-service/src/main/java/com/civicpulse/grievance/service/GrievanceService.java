package com.civicpulse.grievance.service;

import com.civicpulse.grievance.domain.Grievance;
import com.civicpulse.grievance.repository.GrievanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GrievanceService {

    private static final Logger log = LoggerFactory.getLogger(GrievanceService.class);

    private final GrievanceRepository grievanceRepository;

    public GrievanceService(GrievanceRepository grievanceRepository) {
        this.grievanceRepository = grievanceRepository;
    }

    public Grievance submitGrievance(Grievance grievance) {
        int slaDays = calculateSlaDays(grievance.getSeverity());
        grievance.setSlaDays(slaDays);
        grievance.setEscalationLevel(0);
        grievance.setStatus(Grievance.Status.SUBMITTED);
        grievance.setCreatedAt(LocalDateTime.now());
        grievance.setDueDate(LocalDateTime.now().plusDays(slaDays));

        if (grievance.getAssignedDepartment() == null) {
            grievance.setAssignedDepartment(Grievance.Department.UNASSIGNED);
        }

        Grievance saved = grievanceRepository.save(grievance);
        log.info("Grievance submitted successfully: {} (ID: {})", saved.getTitle(), saved.getId());
        return saved;
    }

    public Grievance assignDepartment(UUID grievanceId, Grievance.Department department) {
        Grievance grievance = grievanceRepository.findById(grievanceId)
                .orElseThrow(() -> new IllegalArgumentException("Grievance not found."));

        grievance.setAssignedDepartment(department);
        if (grievance.getStatus() == Grievance.Status.SUBMITTED) {
            grievance.setStatus(Grievance.Status.IN_PROGRESS);
        }
        log.info("Assigned grievance {} to department {}", grievanceId, department);
        return grievanceRepository.save(grievance);
    }

    public Grievance resolveGrievance(UUID grievanceId, String notes) {
        Grievance grievance = grievanceRepository.findById(grievanceId)
                .orElseThrow(() -> new IllegalArgumentException("Grievance not found."));

        grievance.setStatus(Grievance.Status.RESOLVED);
        grievance.setResolvedAt(LocalDateTime.now());
        grievance.setResolutionNotes(notes);
        log.info("Grievance resolved: {} with notes: {}", grievanceId, notes);
        return grievanceRepository.save(grievance);
    }

    public Grievance closeGrievance(UUID grievanceId) {
        Grievance grievance = grievanceRepository.findById(grievanceId)
                .orElseThrow(() -> new IllegalArgumentException("Grievance not found."));

        grievance.setStatus(Grievance.Status.CLOSED);
        log.info("Grievance closed: {}", grievanceId);
        return grievanceRepository.save(grievance);
    }

    public List<Grievance> getAllGrievances() {
        return grievanceRepository.findAll();
    }

    public Optional<Grievance> getGrievanceById(UUID id) {
        return grievanceRepository.findById(id);
    }

    public List<Grievance> getGrievancesByCitizen(UUID citizenId) {
        return grievanceRepository.findByCitizenId(citizenId);
    }

    public void processCitizenRegistrationEvent(Object citizenData) {
        log.info("Kafka consumer: Received 'citizen-registered' event payload. Syncing audit trails: {}", citizenData);
    }

    private int calculateSlaDays(Grievance.Severity severity) {
        if (severity == null) return 5;
        return switch (severity) {
            case CRITICAL -> 1;
            case HIGH -> 2;
            case MEDIUM -> 5;
            case LOW -> 10;
        };
    }
}
