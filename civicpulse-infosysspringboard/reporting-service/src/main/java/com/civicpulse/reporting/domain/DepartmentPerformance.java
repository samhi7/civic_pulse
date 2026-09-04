package com.civicpulse.reporting.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "department_performance")
public class DepartmentPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;

    @Column(name = "resolution_rate", nullable = false)
    private double resolutionRate;

    @Column(name = "sla_compliance", nullable = false)
    private double slaCompliance;

    @Column(name = "avg_response_hours", nullable = false)
    private double avgResponseHours;

    @Column(name = "satisfaction_score", nullable = false)
    private double satisfactionScore;

    @Column(name = "total_cases", nullable = false)
    private int totalCases;

    @Column(name = "resolved_cases", nullable = false)
    private int resolvedCases;

    public DepartmentPerformance() {}

    public DepartmentPerformance(UUID id, String departmentName, double resolutionRate, double slaCompliance,
                                 double avgResponseHours, double satisfactionScore, int totalCases, int resolvedCases) {
        this.id = id;
        this.departmentName = departmentName;
        this.resolutionRate = resolutionRate;
        this.slaCompliance = slaCompliance;
        this.avgResponseHours = avgResponseHours;
        this.satisfactionScore = satisfactionScore;
        this.totalCases = totalCases;
        this.resolvedCases = resolvedCases;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public double getResolutionRate() { return resolutionRate; }
    public void setResolutionRate(double resolutionRate) { this.resolutionRate = resolutionRate; }

    public double getSlaCompliance() { return slaCompliance; }
    public void setSlaCompliance(double slaCompliance) { this.slaCompliance = slaCompliance; }

    public double getAvgResponseHours() { return avgResponseHours; }
    public void setAvgResponseHours(double avgResponseHours) { this.avgResponseHours = avgResponseHours; }

    public double getSatisfactionScore() { return satisfactionScore; }
    public void setSatisfactionScore(double satisfactionScore) { this.satisfactionScore = satisfactionScore; }

    public int getTotalCases() { return totalCases; }
    public void setTotalCases(int totalCases) { this.totalCases = totalCases; }

    public int getResolvedCases() { return resolvedCases; }
    public void setResolvedCases(int resolvedCases) { this.resolvedCases = resolvedCases; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String departmentName;
        private double resolutionRate;
        private double slaCompliance;
        private double avgResponseHours;
        private double satisfactionScore;
        private int totalCases;
        private int resolvedCases;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public Builder resolutionRate(double resolutionRate) { this.resolutionRate = resolutionRate; return this; }
        public Builder slaCompliance(double slaCompliance) { this.slaCompliance = slaCompliance; return this; }
        public Builder avgResponseHours(double avgResponseHours) { this.avgResponseHours = avgResponseHours; return this; }
        public Builder satisfactionScore(double satisfactionScore) { this.satisfactionScore = satisfactionScore; return this; }
        public Builder totalCases(int totalCases) { this.totalCases = totalCases; return this; }
        public Builder resolvedCases(int resolvedCases) { this.resolvedCases = resolvedCases; return this; }

        public DepartmentPerformance build() {
            return new DepartmentPerformance(id, departmentName, resolutionRate, slaCompliance,
                    avgResponseHours, satisfactionScore, totalCases, resolvedCases);
        }
    }
}
