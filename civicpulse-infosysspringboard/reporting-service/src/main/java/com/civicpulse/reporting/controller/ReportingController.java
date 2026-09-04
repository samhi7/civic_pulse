package com.civicpulse.reporting.controller;

import com.civicpulse.reporting.domain.CitizenFeedback;
import com.civicpulse.reporting.domain.ComplianceAudit;
import com.civicpulse.reporting.domain.DepartmentPerformance;
import com.civicpulse.reporting.service.ReportingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/governance-kpis")
    public ResponseEntity<Map<String, Object>> getGovernanceKpis() {
        return ResponseEntity.ok(reportingService.getGovernanceKpis());
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentPerformance>> getDepartmentPerformance() {
        return ResponseEntity.ok(reportingService.getDepartmentPerformanceList());
    }

    @PostMapping("/feedback")
    public ResponseEntity<CitizenFeedback> submitFeedback(@RequestBody CitizenFeedback feedback) {
        CitizenFeedback saved = reportingService.submitFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/feedback")
    public ResponseEntity<List<CitizenFeedback>> getFeedback() {
        return ResponseEntity.ok(reportingService.getRecentFeedback());
    }

    @PostMapping("/export")
    public ResponseEntity<Map<String, Object>> exportReport(
            @RequestParam(required = false, defaultValue = "CSV") String format,
            @RequestParam(required = false, defaultValue = "Municipal Administrator") String requestedBy) {
        Map<String, Object> report = reportingService.exportGovernanceReport(format, requestedBy);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<ComplianceAudit>> getAuditLogs() {
        return ResponseEntity.ok(reportingService.getAuditLogs());
    }
}
