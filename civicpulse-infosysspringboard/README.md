# CivicPulse Nexus — Smart Governance & Citizen Services Platform
**Infosys Springboard Internship Project**

CivicPulse Nexus is an enterprise-grade, cloud-native smart governance and citizen services platform built with a microservices architecture using **Spring Boot (Java 21/26)** and a modern **React 19 + TypeScript + Material-UI** single-page application. The platform digitizes municipal administrative workflows across citizen registration, grievance redressal, public service delivery, welfare disbursements, fiscal budget tracking, and executive governance analytics.

---

## 🏛️ Executive Summary & Milestones Overview

| Milestone | Scope & Domain | Core Capabilities |
|---|---|---|
| **Milestone 1** | **Citizen Identity & Management** | Multi-ward citizen registration, Aadhar ID validation, citizen master directory, and secure profile management. |
| **Milestone 2** | **Grievance Redressal & SLA Engine** | Automated department routing (Water, Electricity, Sanitation, Public Works), real-time SLA deadline monitoring, automated escalation engine, and multi-tier resolution tracking. |
| **Milestone 3** | **Services, Certificates & Welfare** | Two-stage document verification, tamper-evident SHA-256 cryptographic certificate signing, welfare scheme management (e.g. *PM Awas Yojana*), eligibility checks, fund disbursements, and inter-service budget tracking ($47M allocation). |
| **Milestone 4** | **Governance Analytics & Reporting** | Executive analytics dashboard, citizen satisfaction index (4.7/5), service SLA compliance (94%), municipal revenue realization ($12.4M), department benchmarks, report exports (CSV, JSON, PDF), and compliance audit trails. |

---

## 🏗️ System Architecture

CivicPulse Nexus is architected around decentralized, independently scalable microservices communicating via HTTP REST through a centralized API Gateway:

```text
                               +-----------------------------+
                               |     React 19 + TypeScript   |
                               |      Frontend (Port 3000)   |
                               +--------------+--------------+
                                              |
                                              v
                               +-----------------------------+
                               |    Spring Cloud Gateway     |
                               |         (Port 8080)         |
                               +--------------+--------------+
                                              |
        +------------------+------------------+------------------+------------------+------------------+
        |                  |                  |                  |                  |                  |
        v                  v                  v                  v                  v                  v
+---------------+  +---------------+  +---------------+  +---------------+  +---------------+  +---------------+
| Citizen Svc   |  | Grievance Svc |  | Service Mgmt  |  |  Welfare Svc  |  |  Budget Svc   |  | Reporting Svc |
|  (Port 8081)  |  |  (Port 8082)  |  |  (Port 8083)  |  |  (Port 8084)  |  |  (Port 8085)  |  |  (Port 8086)  |
| H2 Database   |  | H2 Database   |  | H2 Database   |  | H2 Database   |  | H2 Database   |  | H2 Database   |
+---------------+  +---------------+  +---------------+  +---------------+  +---------------+  +---------------+
```

### Microservice Catalog

1. **API Gateway (`gateway-service` — Port 8080)**:
   * Reverse proxy and routing hub for all platform microservices.
   * Cross-Origin Resource Sharing (CORS) orchestration and unified path mapping.

2. **Citizen Service (`citizen-service` — Port 8081)**:
   * Citizen master records, contact details, unique Aadhar ID tracking, and ward jurisdictions (Wards 1 through 15).

3. **Grievance Service (`grievance-service` — Port 8082)**:
   * Workflow engine for civic complaints (water supply, roads, sanitation, electricity, public health).
   * Scheduled cron job (`SlaMonitoringService`) for automatic breach detection and administrative escalation.

4. **Service Management Service (`service-management-service` — Port 8083)**:
   * Citizen applications for Birth Certificates, Trade Licenses, and Building Permits.
   * Two-stage verification workflow and SHA-256 digital signature stamping for fraud prevention.

5. **Welfare Service (`welfare-service` — Port 8084)**:
   * Government assistance programs (*PM Awas Yojana*, *Jal Jeevan Urban Water Mission*, *Ayushman Bharat Municipal Health*).
   * Beneficiary qualification checks, direct financial aid disbursements, and inter-service budget audit events.

6. **Budget Service (`budget-service` — Port 8085)**:
   * Municipal department allocations across Housing, Water Sanitation, Public Health, Education Grants, and Public Works.
   * Real-time ledger of expenditures, disbursements, and utilization percentages.

7. **Reporting Service (`reporting-service` — Port 8086 | Milestone 4)**:
   * Aggregation of municipal governance metrics ($12.4M revenue, 94% SLA compliance, 4.7/5 satisfaction rating).
   * Exportable compliance audit logs and citizen reviews feed.

---

## 💻 Tech Stack

* **Backend**: Java 21/26, Spring Boot 3.4.2, Spring Cloud Gateway, Spring Data JPA, Hibernate, H2 In-Memory Database.
* **Frontend**: React 19, TypeScript, Vite 8.2, Material-UI (MUI 9), Emotion, Recharts.
* **DevOps / Build**: Apache Maven (multi-module build), PowerShell automation scripts (`run-dev.ps1`).

---

## 🚀 Quick Start Guide

### Prerequisites
* **Java**: JDK 21 or later (`java -version`)
* **Node.js**: Node 18+ and npm (`node -v`, `npm -v`)
* **Maven**: Apache Maven 3.8+ (`mvn -v`)

### 1. Launch Platform (One-Click Startup)
Open PowerShell in the `civicpulse-infosysspringboard` folder and execute:

```powershell
.\run-dev.ps1
```

The script will automatically:
1. Clean up any stale background processes.
2. Initialize and boot all 7 Spring Boot microservices.
3. Start the Vite React development server on port **3000**.
4. Launch your default browser to **`http://localhost:3000`**.
5. Stream service logs to the `logs/` directory.

---

## 🌐 Access & Role-Based Navigation

Navigate to **`http://localhost:3000`** in your web browser.

### 👤 Citizen Portal
* Select a verified resident profile from the dropdown (e.g. **Ramesh Kumar**, **Priya Sharma**, **Vikram Malhotra**, **Sunita Rao**, or **Amit Patel**).
* **Dashboard (Home)**: View personal Aadhar ID, ward area, and active complaint summary.
* **My Grievances**: File issues with real-time severity levels, descriptions, and ward tags.
* **My Documents**: Apply for civic permits and download certificates signed with SHA-256 cryptographic signatures.
* **Welfare Schemes**: Check eligibility and payout status under government assistance programs.
* **Service Quality & Feedback (Milestone 4)**: Submit 1-to-5 star citizen reviews directly influencing department accountability.

### 🛡️ Municipal Console (Admin)
* Click **Access Municipal Console** for complete administrative authority:
  * **Citizens Directory**: Full resident registry with ward filtering.
  * **Grievance Board**: Reassign cases between municipal departments, log work order notes, resolve cases, and trigger automated SLA breach escalations.
  * **Certificate Approvals**: Perform two-stage document verification and execute cryptographic approvals.
  * **Budget & Welfare**: Approve scheme enrollments, trigger direct fund transfers, and monitor municipal department expenditures ($47M allocation).
  * **Executive Reports & Governance Analytics (Milestone 4)**:
    * Summary KPI Cards: Citizen Satisfaction (4.7/5), Service SLA (94%), Revenue Collected ($12.4M).
    * Detailed Validation Tabs: *Governance KPIs*, *Department Performance Index*, *Revenue Realization ($12.4M)*, *Grievance Trends (MTTR 47 hrs)*, and *Verified Citizen Reviews*.
    * Interactive Actions: `[Export Report]` (CSV, JSON, PDF), `[Drill Down]` (department-level benchmarks), and `[Share]` (one-click clipboard link).

---

## 📡 REST API Reference (via Gateway `http://localhost:8080`)

| Service | Method | Endpoint | Description |
|---|---|---|---|
| **Citizen** | `GET` | `/api/v1/citizens` | List all registered residents |
| **Citizen** | `POST` | `/api/v1/citizens` | Register a new citizen |
| **Grievance** | `GET` | `/api/v1/grievances` | Retrieve all grievances |
| **Grievance** | `POST` | `/api/v1/grievances/{id}/assign` | Reassign to department |
| **Grievance** | `POST` | `/api/v1/grievances/{id}/resolve` | Resolve grievance with work order notes |
| **Grievance** | `POST` | `/api/v1/grievances/{id}/trigger-escalation` | Trigger SLA breach and escalate to Admin |
| **Services** | `GET` | `/api/v1/services/applications` | List certificate/permit applications |
| **Services** | `POST` | `/api/v1/services/applications/{id}/verify` | Document verification stage |
| **Services** | `POST` | `/api/v1/services/applications/{id}/approve` | Issue digitally signed certificate |
| **Welfare** | `GET` | `/api/v1/welfare/schemes` | List assistance schemes |
| **Welfare** | `POST` | `/api/v1/welfare/beneficiaries/{id}/disburse` | Disburse welfare funds to citizen |
| **Budget** | `GET` | `/api/v1/budgets` | Fetch departmental budget utilization |
| **Reports** | `GET` | `/api/v1/reports/governance-kpis` | Executive governance KPI synthesis |
| **Reports** | `GET` | `/api/v1/reports/departments` | Department benchmarks & SLA ranking |
| **Reports** | `POST` | `/api/v1/reports/feedback` | Submit citizen satisfaction review |
| **Reports** | `GET` | `/api/v1/reports/export?format=CSV` | Export compliance report & log audit |

---

## 🧪 Testing & Verification

Each microservice contains dedicated JUnit 5 test suites. To execute the automated tests across all 7 services:

```powershell
mvn test
```

All 22 unit tests across Citizen, Grievance, Service Management, Welfare, Budget, and Reporting modules pass with zero failures.

---

## 👥 Authors & Acknowledgments
* **Project**: CivicPulse Nexus Smart Governance Platform
* **Internship Program**: Infosys Springboard
* **Developer**: [@samhi7](https://github.com/samhi7)
