package com.civicpulse.reporting;

import com.civicpulse.reporting.domain.CitizenFeedback;
import com.civicpulse.reporting.domain.ComplianceAudit;
import com.civicpulse.reporting.domain.DepartmentPerformance;
import com.civicpulse.reporting.repository.CitizenFeedbackRepository;
import com.civicpulse.reporting.repository.ComplianceAuditRepository;
import com.civicpulse.reporting.repository.DepartmentPerformanceRepository;
import com.civicpulse.reporting.service.ReportingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportingServiceTest {

    @Mock
    private DepartmentPerformanceRepository departmentRepo;

    @Mock
    private CitizenFeedbackRepository feedbackRepo;

    @Mock
    private ComplianceAuditRepository auditRepo;

    @InjectMocks
    private ReportingService reportingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGovernanceKpis_ReturnsExpectedMetrics() {
        when(feedbackRepo.findAll()).thenReturn(List.of(
                CitizenFeedback.builder().rating(5).build(),
                CitizenFeedback.builder().rating(4).build()
        ));

        Map<String, Object> kpis = reportingService.getGovernanceKpis();

        assertNotNull(kpis);
        assertEquals(4.5, kpis.get("citizenSatisfaction"));
        assertEquals(94.0, kpis.get("serviceSla"));
        assertEquals(12.4, kpis.get("revenueCollected"));
        assertEquals("24.7K", kpis.get("servicesTotal"));
        assertEquals("12.4K", kpis.get("grievancesFiled"));
        assertEquals(87.0, kpis.get("budgetUtilizationPct"));
    }

    @Test
    void getDepartmentPerformanceList_ReturnsAll() {
        List<DepartmentPerformance> list = List.of(
                DepartmentPerformance.builder().departmentName("Water Dept").resolutionRate(94.0).build(),
                DepartmentPerformance.builder().departmentName("Health Dept").resolutionRate(91.0).build()
        );
        when(departmentRepo.findAll()).thenReturn(list);

        List<DepartmentPerformance> result = reportingService.getDepartmentPerformanceList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Water Dept", result.get(0).getDepartmentName());
    }

    @Test
    void submitFeedback_SavesAndCreatesAuditRecord() {
        CitizenFeedback feedback = CitizenFeedback.builder()
                .citizenId(UUID.randomUUID())
                .citizenName("Ramesh Kumar")
                .category("Water Supply")
                .rating(5)
                .comments("Great quick service")
                .build();

        when(feedbackRepo.save(any(CitizenFeedback.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CitizenFeedback saved = reportingService.submitFeedback(feedback);

        assertNotNull(saved);
        assertEquals("Ramesh Kumar", saved.getCitizenName());
        verify(feedbackRepo, times(1)).save(feedback);
        verify(auditRepo, times(1)).save(any(ComplianceAudit.class));
    }

    @Test
    void exportGovernanceReport_GeneratesReportAndAudit() {
        when(departmentRepo.findAll()).thenReturn(Collections.emptyList());
        when(feedbackRepo.findAll()).thenReturn(Collections.emptyList());

        Map<String, Object> report = reportingService.exportGovernanceReport("PDF", "Admin Tester");

        assertNotNull(report);
        assertEquals("PDF", report.get("format"));
        assertEquals("Admin Tester", report.get("generatedBy"));
        verify(auditRepo, times(1)).save(any(ComplianceAudit.class));
    }
}
