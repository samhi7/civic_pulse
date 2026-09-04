package com.civicpulse.citizen.service;

import com.civicpulse.citizen.domain.Citizen;
import com.civicpulse.citizen.repository.CitizenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CitizenService {

    private static final Logger log = LoggerFactory.getLogger(CitizenService.class);

    private final CitizenRepository citizenRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public CitizenService(CitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    public Citizen registerCitizen(Citizen citizen) {
        // Validate Aadhar number format
        if (citizen.getAadharNumber() == null || !citizen.getAadharNumber().matches("^\\d{4}-\\d{4}-\\d{4}$")) {
            throw new IllegalArgumentException("Invalid Aadhar number format. Must be XXXX-XXXX-XXXX");
        }

        // Check if Aadhar is already registered
        Optional<Citizen> existing = citizenRepository.findByAadharNumber(citizen.getAadharNumber());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Citizen with this Aadhar number is already registered.");
        }

        Citizen savedCitizen = citizenRepository.save(citizen);
        log.info("Registered new citizen: {} (ID: {})", savedCitizen.getName(), savedCitizen.getId());

        // Simulate Kafka Event emission asynchronously to Grievance Service
        publishRegistrationEvent(savedCitizen);

        return savedCitizen;
    }

    public List<Citizen> getAllCitizens() {
        return citizenRepository.findAll();
    }

    public Optional<Citizen> getCitizenById(UUID id) {
        return citizenRepository.findById(id);
    }

    public Optional<Citizen> getCitizenByAadhar(String aadhar) {
        return citizenRepository.findByAadharNumber(aadhar);
    }

    public List<Citizen> searchCitizensByName(String name) {
        return citizenRepository.findByNameContainingIgnoreCase(name);
    }

    private void publishRegistrationEvent(Citizen citizen) {
        CompletableFuture.runAsync(() -> {
            try {
                String grievanceServiceUrl = "http://localhost:8082/api/v1/grievances/events/citizen-registered";
                log.info("Simulating Kafka event: citizen-registered to {}", grievanceServiceUrl);
                restTemplate.postForObject(grievanceServiceUrl, citizen, Void.class);
                log.info("Successfully dispatched simulated Kafka event for citizen: {}", citizen.getName());
            } catch (Exception e) {
                log.warn("Failed to dispatch simulated Kafka event (Grievance service might be offline): {}", e.getMessage());
            }
        });
    }
}
