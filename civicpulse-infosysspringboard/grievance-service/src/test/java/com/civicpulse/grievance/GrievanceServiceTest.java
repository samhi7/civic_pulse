package com.civicpulse.grievance;

import com.civicpulse.grievance.domain.Grievance;
import com.civicpulse.grievance.repository.GrievanceRepository;
import com.civicpulse.grievance.service.GrievanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrievanceServiceTest {

    @Mock
    private GrievanceRepository grievanceRepository;

    @InjectMocks
    private GrievanceService grievanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitGrievance_CriticalSeverity_SetsOneDaySla() {
        Grievance grievance = Grievance.builder()
                .citizenId(UUID.randomUUID())
                .citizenName("Ramesh Kumar")
                .title("Power Outage")
                .category(Grievance.Category.ELECTRICITY)
                .severity(Grievance.Severity.CRITICAL)
                .build();

        when(grievanceRepository.save(any(Grievance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grievance result = grievanceService.submitGrievance(grievance);

        assertNotNull(result);
        assertEquals(1, result.getSlaDays());
        assertEquals(Grievance.Status.SUBMITTED, result.getStatus());
    }

    @Test
    void submitGrievance_HighSeverity_SetsTwoDaysSla() {
        Grievance grievance = Grievance.builder()
                .citizenId(UUID.randomUUID())
                .citizenName("Ramesh Kumar")
                .title("Water Leakage")
                .category(Grievance.Category.WATER_SUPPLY)
                .severity(Grievance.Severity.HIGH)
                .build();

        when(grievanceRepository.save(any(Grievance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grievance result = grievanceService.submitGrievance(grievance);

        assertNotNull(result);
        assertEquals(2, result.getSlaDays());
        assertEquals(Grievance.Status.SUBMITTED, result.getStatus());
    }

    @Test
    void assignDepartment_ChangesStatusToInProgress() {
        UUID grievanceId = UUID.randomUUID();
        Grievance grievance = Grievance.builder()
                .id(grievanceId)
                .citizenId(UUID.randomUUID())
                .citizenName("Ramesh Kumar")
                .status(Grievance.Status.SUBMITTED)
                .assignedDepartment(Grievance.Department.UNASSIGNED)
                .build();

        when(grievanceRepository.findById(grievanceId)).thenReturn(Optional.of(grievance));
        when(grievanceRepository.save(any(Grievance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grievance result = grievanceService.assignDepartment(grievanceId, Grievance.Department.WATER_DEPT);

        assertNotNull(result);
        assertEquals(Grievance.Department.WATER_DEPT, result.getAssignedDepartment());
        assertEquals(Grievance.Status.IN_PROGRESS, result.getStatus());
    }

    @Test
    void resolveGrievance_SetsResolvedStatusAndNotes() {
        UUID grievanceId = UUID.randomUUID();
        Grievance grievance = Grievance.builder()
                .id(grievanceId)
                .citizenId(UUID.randomUUID())
                .citizenName("Ramesh Kumar")
                .status(Grievance.Status.IN_PROGRESS)
                .build();

        when(grievanceRepository.findById(grievanceId)).thenReturn(Optional.of(grievance));
        when(grievanceRepository.save(any(Grievance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grievance result = grievanceService.resolveGrievance(grievanceId, "Fixed water pipe leaks.");

        assertNotNull(result);
        assertEquals(Grievance.Status.RESOLVED, result.getStatus());
        assertEquals("Fixed water pipe leaks.", result.getResolutionNotes());
        assertNotNull(result.getResolvedAt());
    }
}
