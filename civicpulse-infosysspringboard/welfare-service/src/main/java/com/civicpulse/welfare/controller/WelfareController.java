package com.civicpulse.welfare.controller;

import com.civicpulse.welfare.domain.Beneficiary;
import com.civicpulse.welfare.domain.WelfareScheme;
import com.civicpulse.welfare.service.WelfareService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/welfare")
public class WelfareController {

    private final WelfareService welfareService;

    public WelfareController(WelfareService welfareService) {
        this.welfareService = welfareService;
    }

    @PostMapping("/schemes")
    public ResponseEntity<WelfareScheme> createScheme(@RequestBody WelfareScheme scheme) {
        WelfareScheme created = welfareService.createScheme(scheme);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/schemes")
    public ResponseEntity<List<WelfareScheme>> getAllSchemes() {
        return ResponseEntity.ok(welfareService.getAllSchemes());
    }

    @GetMapping("/schemes/{id}")
    public ResponseEntity<WelfareScheme> getSchemeById(@PathVariable UUID id) {
        return welfareService.getSchemeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/schemes/{id}/enroll")
    public ResponseEntity<?> enrollBeneficiary(@PathVariable("id") UUID schemeId, @RequestBody Beneficiary beneficiary) {
        try {
            beneficiary.setSchemeId(schemeId);
            Beneficiary enrolled = welfareService.enrollBeneficiary(beneficiary);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrolled);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/schemes/{id}/beneficiaries")
    public ResponseEntity<List<Beneficiary>> getBeneficiariesForScheme(@PathVariable("id") UUID schemeId) {
        return ResponseEntity.ok(welfareService.getBeneficiariesForScheme(schemeId));
    }

    @PostMapping("/beneficiaries/{id}/verify")
    public ResponseEntity<Beneficiary> verifyEligibility(@PathVariable UUID id) {
        try {
            Beneficiary verified = welfareService.verifyEligibility(id);
            return ResponseEntity.ok(verified);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/beneficiaries/{id}/disburse")
    public ResponseEntity<Beneficiary> disburseFunds(
            @PathVariable UUID id, 
            @RequestParam BigDecimal amount) {
        try {
            Beneficiary updated = welfareService.disburseFunds(id, amount);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    static record ErrorResponse(String error) {}
}
