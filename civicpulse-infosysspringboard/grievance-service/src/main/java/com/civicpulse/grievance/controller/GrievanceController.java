package com.civicpulse.grievance.controller;

import com.civicpulse.grievance.domain.Grievance;
import com.civicpulse.grievance.service.GrievanceService;
import com.civicpulse.grievance.service.SlaMonitoringService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grievances")
public class GrievanceController {

    private final GrievanceService grievanceService;
    private final SlaMonitoringService slaMonitoringService;

    public GrievanceController(GrievanceService grievanceService, SlaMonitoringService slaMonitoringService) {
        this.grievanceService = grievanceService;
        this.slaMonitoringService = slaMonitoringService;
    }

    @PostMapping
    public ResponseEntity<Grievance> submitGrievance(@RequestBody Grievance grievance) {
        Grievance submitted = grievanceService.submitGrievance(grievance);
        return ResponseEntity.status(HttpStatus.CREATED).body(submitted);
    }

    @GetMapping
    public ResponseEntity<List<Grievance>> getAllGrievances(
            @RequestParam(required = false) Grievance.Status status,
            @RequestParam(required = false) Grievance.Department department) {
        
        List<Grievance> grievances = grievanceService.getAllGrievances();
        
        if (status != null) {
            grievances = grievances.stream().filter(g -> g.getStatus() == status).toList();
        }
        if (department != null) {
            grievances = grievances.stream().filter(g -> g.getAssignedDepartment() == department).toList();
        }
        
        return ResponseEntity.ok(grievances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grievance> getGrievanceById(@PathVariable UUID id) {
        return grievanceService.getGrievanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Grievance> assignDepartment(
            @PathVariable UUID id, 
            @RequestParam Grievance.Department department) {
        try {
            Grievance updated = grievanceService.assignDepartment(id, department);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Grievance> resolveGrievance(
            @PathVariable UUID id, 
            @RequestBody ResolutionRequest request) {
        try {
            Grievance updated = grievanceService.resolveGrievance(id, request.notes());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Grievance> closeGrievance(@PathVariable UUID id) {
        try {
            Grievance updated = grievanceService.closeGrievance(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/trigger-escalation")
    public ResponseEntity<Grievance> triggerEscalation(@PathVariable UUID id) {
        return grievanceService.getGrievanceById(id)
                .map(g -> {
                    slaMonitoringService.escalateGrievance(g);
                    return ResponseEntity.ok(g);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/events/citizen-registered")
    public ResponseEntity<Void> receiveCitizenRegisteredEvent(@RequestBody Object citizenData) {
        grievanceService.processCitizenRegistrationEvent(citizenData);
        return ResponseEntity.ok().build();
    }

    static record ResolutionRequest(String notes) {}
}
