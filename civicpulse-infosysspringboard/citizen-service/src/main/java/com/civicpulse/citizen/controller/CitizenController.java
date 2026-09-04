package com.civicpulse.citizen.controller;

import com.civicpulse.citizen.domain.Citizen;
import com.civicpulse.citizen.service.CitizenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/citizens")
public class CitizenController {

    private final CitizenService citizenService;

    public CitizenController(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @PostMapping
    public ResponseEntity<?> registerCitizen(@RequestBody Citizen citizen) {
        try {
            Citizen registered = citizenService.registerCitizen(citizen);
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Citizen>> getCitizens(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String aadhar) {
        
        if (aadhar != null && !aadhar.isEmpty()) {
            return citizenService.getCitizenByAadhar(aadhar)
                    .map(c -> ResponseEntity.ok(List.of(c)))
                    .orElse(ResponseEntity.ok(List.of()));
        }
        
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(citizenService.searchCitizensByName(search));
        }

        return ResponseEntity.ok(citizenService.getAllCitizens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Citizen> getCitizenById(@PathVariable UUID id) {
        return citizenService.getCitizenById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    static record ErrorResponse(String error) {}
}
