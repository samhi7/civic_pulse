# CivicPulse - Civic Tech Web Application (Role-Based Edition)

CivicPulse is a Spring Boot web application that connects citizens with municipal departments to report and track neighborhood issues (potholes, water leaks, broken streetlights, etc.).

This application features role-based access, letting standard citizens submit issues with location data and browse other public issues, while enabling administrators to resolve them using a resolution control console.

---

## 🔑 Demo Credentials

To test the role-based functionality immediately, the database is pre-seeded with these accounts:

### 1. Municipal Administrator
*   **Email**: `admin@civicpulse.gov`
*   **Password**: `adminpassword`
*   *Role*: `ADMIN` (Access to global registry and resolution actions)

### 2. Standard Citizen
*   **Email**: `citizen@example.com`
*   **Password**: `password123`
*   *Role*: `USER` (Access to personal reports, complaint submission, and public community boards)

---

## 🛠️ Project Structure

```text
CivicPulse/
├── pom.xml                               # Maven project dependencies (JPA & H2 added)
├── README.md                             # Documentation and setup instructions
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── civicpulse/
        │               ├── CivicPulseApplication.java   # App Entry Point
        │               ├── config/
        │               │   └── DataInitializer.java     # Database Seeder (Seeds users/complaints)
        │               ├── controller/
        │               │   └── MainController.java      # Role-based Routing & Mappings
        │               ├── model/
        │               │   ├── User.java                # JPA Entity for Users (mapped with roles)
        │               │   └── Complaint.java           # JPA Entity for Complaints (mapped with location)
        │               ├── repository/
        │               │   ├── UserRepository.java      # User database queries
        │               │   └── ComplaintRepository.java # Complaint database queries
        │               └── service/
        │                   ├── UserService.java         # Hashing & Authentication logic
        │                   └── ComplaintService.java     # Complaint lifecycle & metrics
        └── resources/
            ├── application.properties     # H2 Database URL & Spring JPA configurations
            ├── static/
            │   └── css/
            │       └── style.css          # Sleek Indigo & Cyan visual styling
            └── templates/
                ├── home.html              # Homepage
                ├── login.html             # Login form
                ├── register.html          # Registration form
                ├── dashboard.html         # Citizen Dashboard (My Reports & Community Tabs)
                ├── report.html            # Report Complaint form (with Location address field)
                └── admin_dashboard.html   # Admin Console (Resolution actions list)
```

---

## How to Run the Application

### Option 1: Run via IntelliJ IDEA (Recommended)
1. Open **IntelliJ IDEA**.
2. Select **Open** and choose the `CivicPulse/` directory.
3. Allow IntelliJ to import Maven dependencies (takes about a minute).
4. Navigate to `src/main/java/com/example/civicpulse/CivicPulseApplication.java`.
5. Click the green **Run** button next to the `main` method.

### Option 2: Run via Command Line (if Maven is installed)
```bash
cd CivicPulse
mvn spring-boot:run
```

Once started, the application will be available at:
👉 **[http://localhost:8080/](http://localhost:8080/)**

To inspect the in-memory H2 database tables manually, you can visit:
👉 **[http://localhost:8080/h2-console](http://localhost:8080/h2-console)**
*(JDBC URL: `jdbc:h2:mem:civicpulsedb`, Username: `sa`, Password: empty)*
