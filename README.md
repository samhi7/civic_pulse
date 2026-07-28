# 🏛️ CivicPulse - Smart Civic Issue Reporting & Management Platform

**CivicPulse** is a modern, full-stack Spring Boot web application designed to connect citizens with municipal departments. It empowers citizens to report neighborhood issues (potholes, water leaks, broken streetlights, waste accumulation) with geolocation data and multi-media evidence, while enabling field officers and municipal administrators to manage, dispatch, and resolve complaints efficiently.

---

## 🔑 Demo Credentials

To test the role-based functionality immediately, the database is pre-seeded with these demo accounts:

| Role | Email | Password | Access & Permissions |
| :--- | :--- | :--- | :--- |
| **Municipal Admin** | `admin@civicpulse.gov` | `adminpassword` | Global registry, officer dispatch, user directory, system audit logs |
| **Field Officer** | `officer@civicpulse.gov` | `officerpassword` | Assigned task queue, progress tracking, status update & photo proof |
| **Standard Citizen** | `citizen@example.com` | `password123` | Personal complaint dashboard, 5-step wizard, interactive city map |

---

## ✨ Key Features

### 👤 Citizen Portal
- **5-Step Report Wizard**: Interactive wizard supporting issue categorization, GPS pin locator, multi-image evidence, and video uploads.
- **Interactive Spatial City Map**: Live Leaflet GIS map displaying reported issue pins across the city.
- **Community Board & Engagement**: Citizens can upvote/support community reports and participate in discussion threads.
- **Onboarding Wizard**: First-time login experience to set city, locality, phone, and avatar.

### 👷 Field Officer Console
- **Assigned Tasks Queue**: Real-time list of assigned field complaints filtered by department.
- **Status & Proof Submission**: Ability to mark tasks *In Progress* or *Resolved* and upload resolution photo proof.

### 🛡️ Administrator Console
- **Resolution Control Dashboard**: High-level resolution analytics and metrics.
- **Officer Dispatch**: Assign unassigned complaints to specific field officers and departments.
- **User Ledger & Audit Logs**: Comprehensive user directory and real-time audit trail tracking all actions.

---

## 🛠️ Tech Stack & Architecture

- **Backend**: Java 17, Spring Boot 3.3.1 (Spring MVC, Spring Data JPA)
- **Database**: H2 In-Memory Database (`jdbc:h2:mem:civicpulsedb`)
- **Frontend**: HTML5, Thymeleaf, Bootstrap 5, Leaflet.js (GIS Mapping), Vanilla CSS
- **Build Tool**: Maven

---

## 📁 Repository Structure

```text
Milestone1/
├── README.md                             # Main GitHub repository documentation
└── CivicPulse/                           # Primary Spring Boot Web Application
    ├── pom.xml                           # Maven project dependencies
    ├── README.md                         # Application module guide
    └── src/
        └── main/
            ├── java/
            │   └── com/
            │       └── example/
            │           └── civicpulse/
            │               ├── CivicPulseApplication.java   # Main App Entry Point
            │               ├── config/                      # Data Seeder & App Configs
            │               ├── controller/                  # Role-Based MVC Routing & Controllers
            │               ├── model/                       # JPA Entities (User, Complaint, AuditLog)
            │               ├── repository/                  # Spring Data Repositories
            │               └── service/                     # Business Logic Services
            └── resources/
                ├── application.properties        # Database & JPA settings
                ├── static/                       # CSS styles, JS, and upload storage
                └── templates/                    # Thymeleaf HTML5 templates
```

---

## 🚀 How to Run the Application

### Option 1: Command Line (Maven)

```bash
cd CivicPulse
mvn spring-boot:run
```

Once started, access the web app at:
👉 **[http://localhost:8080/](http://localhost:8080/)**

To inspect the H2 database console:
👉 **[http://localhost:8080/h2-console](http://localhost:8080/h2-console)**
*(JDBC URL: `jdbc:h2:mem:civicpulsedb` \| Username: `sa` \| Password: empty)*

---

### Option 2: IntelliJ IDEA or Eclipse

1. Open **IntelliJ IDEA**.
2. Select **Open** and choose the `CivicPulse/` folder.
3. Wait for Maven to import dependencies.
4. Open `src/main/java/com/example/civicpulse/CivicPulseApplication.java`.
5. Run the `main` method.
