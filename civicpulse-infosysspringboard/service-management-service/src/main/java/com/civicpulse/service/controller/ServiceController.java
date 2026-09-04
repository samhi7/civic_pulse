package com.civicpulse.service.controller;

import com.civicpulse.service.domain.Application;
import com.civicpulse.service.domain.Certificate;
import com.civicpulse.service.domain.Permit;
import com.civicpulse.service.service.ServiceManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServiceManagementService serviceManagementService;

    public ServiceController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    @PostMapping("/applications")
    public ResponseEntity<Application> submitApplication(@RequestBody Application application) {
        Application submitted = serviceManagementService.submitApplication(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(submitted);
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(serviceManagementService.getAllApplications());
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable UUID id) {
        return serviceManagementService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/applications/{id}/verify")
    public ResponseEntity<Application> verifyDocuments(@PathVariable UUID id) {
        try {
            Application verified = serviceManagementService.verifyDocuments(id);
            return ResponseEntity.ok(verified);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/applications/{id}/approve")
    public ResponseEntity<Application> approveApplication(@PathVariable UUID id) {
        try {
            Application approved = serviceManagementService.approveApplication(id);
            return ResponseEntity.ok(approved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/certificates/{id}/download")
    public ResponseEntity<Certificate> downloadCertificate(@PathVariable UUID id) {
        try {
            Certificate updated = serviceManagementService.trackDownload(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/applications/{id}/document")
    public ResponseEntity<?> getAssociatedDocument(@PathVariable UUID id) {
        Optional<Certificate> cert = serviceManagementService.getCertificateByApplicationId(id);
        if (cert.isPresent()) {
            return ResponseEntity.ok(new DocumentResponse("CERTIFICATE", cert.get()));
        }
        
        Optional<Permit> permit = serviceManagementService.getPermitByApplicationId(id);
        if (permit.isPresent()) {
            return ResponseEntity.ok(new DocumentResponse("PERMIT", permit.get()));
        }

        return ResponseEntity.notFound().build();
    }

    static record DocumentResponse(String type, Object document) {}
}
