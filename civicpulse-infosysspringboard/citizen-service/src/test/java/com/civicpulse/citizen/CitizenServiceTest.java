package com.civicpulse.citizen;

import com.civicpulse.citizen.domain.Citizen;
import com.civicpulse.citizen.repository.CitizenRepository;
import com.civicpulse.citizen.service.CitizenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CitizenServiceTest {

    @Mock
    private CitizenRepository citizenRepository;

    @InjectMocks
    private CitizenService citizenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerCitizen_ValidData_Success() {
        Citizen citizen = Citizen.builder()
                .name("Ramesh Kumar")
                .aadharNumber("1234-5678-9012")
                .phone("9876543210")
                .ward("Ward 12")
                .build();

        when(citizenRepository.findByAadharNumber(anyString())).thenReturn(Optional.empty());
        when(citizenRepository.save(any(Citizen.class))).thenReturn(citizen);

        Citizen result = citizenService.registerCitizen(citizen);

        assertNotNull(result);
        assertEquals("Ramesh Kumar", result.getName());
        verify(citizenRepository, times(1)).save(any(Citizen.class));
    }

    @Test
    void registerCitizen_InvalidAadhar_ThrowsException() {
        Citizen citizen = Citizen.builder()
                .name("Ramesh Kumar")
                .aadharNumber("1234-5678-90") // Invalid length
                .phone("9876543210")
                .ward("Ward 12")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citizenService.registerCitizen(citizen);
        });

        assertEquals("Invalid Aadhar number format. Must be XXXX-XXXX-XXXX", exception.getMessage());
        verify(citizenRepository, never()).save(any(Citizen.class));
    }

    @Test
    void registerCitizen_DuplicateAadhar_ThrowsException() {
        Citizen citizen = Citizen.builder()
                .name("Ramesh Kumar")
                .aadharNumber("1234-5678-9012")
                .phone("9876543210")
                .ward("Ward 12")
                .build();

        when(citizenRepository.findByAadharNumber("1234-5678-9012")).thenReturn(Optional.of(citizen));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citizenService.registerCitizen(citizen);
        });

        assertEquals("Citizen with this Aadhar number is already registered.", exception.getMessage());
        verify(citizenRepository, never()).save(any(Citizen.class));
    }
}
