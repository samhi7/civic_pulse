package com.civicpulse.grievance.service;

import com.civicpulse.grievance.domain.Grievance;
import com.civicpulse.grievance.repository.GrievanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SlaMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(SlaMonitoringService.class);

    private final GrievanceRepository grievanceRepository;

    public SlaMonitoringService(GrievanceRepository grievanceRepository) {
        this.grievanceRepository = grievanceRepository;
    }

    // Check every 60 seconds
    @Scheduled(fixedDelay = 60000)
    public void checkSlaBreaches() {
        log.info("Running automated SLA breach and escalation scan...");
        LocalDateTime now = LocalDateTime.now();
        
        List<Grievance> overdueGrievances = grievanceRepository.findByStatusNotAndDueDateBefore(
                Grievance.Status.RESOLVED, now
        );

        for (Grievance grievance : overdueGrievances) {
            if (grievance.getStatus() != Grievance.Status.CLOSED) {
                escalateGrievance(grievance);
            }
        }
    }

    public void escalateGrievance(Grievance grievance) {
        int currentLevel = grievance.getEscalationLevel();
        grievance.setEscalationLevel(currentLevel + 1);
        
        // Reassign to Admin Department on escalation
        grievance.setAssignedDepartment(Grievance.Department.ADMIN_DEPT);
        
        // Extend due date slightly for next escalation checks (1 day)
        grievance.setDueDate(LocalDateTime.now().plusDays(1));
        
        grievanceRepository.save(grievance);
        log.warn("SLA BREACH: Grievance '{}' (ID: {}) escalated to Level {} and assigned to ADMIN_DEPT.",
                grievance.getTitle(), grievance.getId(), grievance.getEscalationLevel());
    }
}
