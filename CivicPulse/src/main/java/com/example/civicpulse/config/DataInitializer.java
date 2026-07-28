package com.example.civicpulse.config;

import com.example.civicpulse.model.AuditLog;
import com.example.civicpulse.model.Complaint;
import com.example.civicpulse.model.User;
import com.example.civicpulse.repository.ComplaintRepository;
import com.example.civicpulse.service.ComplaintService;
import com.example.civicpulse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final ComplaintRepository complaintRepository;
    private final ComplaintService complaintService;

    @Autowired
    public DataInitializer(UserService userService, ComplaintRepository complaintRepository, ComplaintService complaintService) {
        this.userService = userService;
        this.complaintRepository = complaintRepository;
        this.complaintService = complaintService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed System Admin
        User admin = userService.findByEmail("admin@civicpulse.gov");
        if (admin == null) {
            admin = new User("System Administrator", "admin@civicpulse.gov", "adminpassword", "adminpassword");
            admin.setRole("ADMIN");
            admin.setFirstTime(false);
            admin.setCity("Metro City");
            admin.setLocality("Administrative Block");
            admin.setPhone("+1 555-0100");
            userService.register(admin);
            System.out.println(">>> Database seeded with Admin user: admin@civicpulse.gov");
        }

        // 2. Seed Field Officer
        User officer = userService.findByEmail("officer@civicpulse.gov");
        if (officer == null) {
            officer = new User("Officer Frank", "officer@civicpulse.gov", "officerpassword", "officerpassword");
            officer.setRole("OFFICER");
            officer.setFirstTime(false);
            officer.setCity("Metro City");
            officer.setLocality("Municipal Headquarters");
            officer.setPhone("+1 555-0155");
            userService.register(officer);
            System.out.println(">>> Database seeded with Field Officer: officer@civicpulse.gov");
        }

        // Fetch persisted entities
        User savedAdmin = userService.findByEmail("admin@civicpulse.gov");
        User savedOfficer = userService.findByEmail("officer@civicpulse.gov");

        // 3. Seed Citizen Users & Complaints
        User citizen = userService.findByEmail("citizen@example.com");
        if (citizen == null) {
            // Seed Jane Doe (Active Citizen, Onboarding Bypassed)
            User jane = new User("Jane Doe", "citizen@example.com", "password123", "password123");
            jane.setRole("USER");
            jane.setFirstTime(false);
            jane.setCity("Metro City");
            jane.setLocality("Downtown");
            jane.setPhone("+1 555-0199");
            userService.register(jane);

            // Seed John Smith (Active Citizen, Onboarding Bypassed)
            User john = new User("John Smith", "john@example.com", "password123", "password123");
            john.setRole("USER");
            john.setFirstTime(false);
            john.setCity("Metro City");
            john.setLocality("Green Valley");
            john.setPhone("+1 555-0244");
            userService.register(john);

            User savedJane = userService.findByEmail("citizen@example.com");
            User savedJohn = userService.findByEmail("john@example.com");

            // Seed sample complaints
            Complaint c1 = new Complaint(
                "CP-1001", 
                "Road Damage", 
                "Assigned", 
                "Large pothole blocking main lane", 
                "A deep pothole has emerged near the crosswalk, causing cars to swerve dangerously.", 
                "104 Baker Street Crossing",
                savedJohn
            );
            c1.setLatitude(40.7128);
            c1.setLongitude(-74.0060);
            c1.setDepartment("Road Maintenance Department");
            c1.setAssignedOfficer(savedOfficer);
            c1.setAdminRemarks("Officer assigned for repair assessment.");
            c1.setBeforePhotoUrl("https://images.unsplash.com/photo-1515162305285-0293e4767cc2?w=800&auto=format&fit=crop&q=60");
            c1.getSupportingUsers().add(savedJohn);
            complaintRepository.save(c1);

            Complaint c2 = new Complaint(
                "CP-1002", 
                "Street Light", 
                "Completed", 
                "Flickering streetlights outside houses", 
                "Three consecutive streetlights are out, leaving the sidewalk completely pitch black at night.", 
                "15 Elm Avenue",
                savedJohn
            );
            c2.setLatitude(40.7135);
            c2.setLongitude(-74.0080);
            c2.setDepartment("Electricity Board");
            c2.setAssignedOfficer(savedOfficer);
            c2.setAdminRemarks("Replaced blown streetlight fuses.");
            c2.setOfficerRemarks("Replaced 3 mercury lamps with high-power LEDs. Sidewalk now fully illuminated.");
            c2.setBeforePhotoUrl("https://images.unsplash.com/photo-1509024644558-2f56ce76c490?w=800&auto=format&fit=crop&q=60");
            c2.setAfterPhotoUrl("https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=800&auto=format&fit=crop&q=60");
            c2.setCitizenRating(5);
            c2.setCitizenFeedback("Super fast resolution, thank you officer!");
            complaintRepository.save(c2);

            Complaint c3 = new Complaint(
                "CP-1003", 
                "Garbage", 
                "Work Started", 
                "Trash pileup near community playground", 
                "Overflowing bins have resulted in trash bags spilling into the children's park area.", 
                "Central Park Kids Play Area",
                savedJohn
            );
            c3.setLatitude(40.7150);
            c3.setLongitude(-74.0095);
            c3.setDepartment("Waste Management");
            c3.setAssignedOfficer(savedOfficer);
            c3.setAdminRemarks("Dispatched cleanup truck.");
            c3.setOfficerRemarks("Truck deployed, currently clearing the playground.");
            c3.getSupportingUsers().add(savedJane);
            c3.getSupportingUsers().add(savedJohn);
            complaintRepository.save(c3);

            Complaint c4 = new Complaint(
                "CP-1004", 
                "Water Leakage", 
                "Pending", 
                "Burst water pipe flooding sidewalk", 
                "A large volume of clean drinking water is leaking from a rusty joint onto the sidewalk.", 
                "98 Lincoln Road Sidewalk",
                savedJohn
            );
            c4.setLatitude(40.7110);
            c4.setLongitude(-74.0050);
            complaintRepository.save(c4);

            // Seed initial audit log actions
            complaintService.logAction("Database Initialized", "Seeded default Admin, Officer, and initial Citizen mock complaints.", savedAdmin);
            complaintService.logAction("System Startup", "CivicPulse Smart Civic Issue Reporting System online.", savedAdmin);
            
            System.out.println(">>> Database seeded with Citizen users, audit logs, and 4 detailed complaints.");
        }
    }
}
