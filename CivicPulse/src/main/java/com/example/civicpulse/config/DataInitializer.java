package com.example.civicpulse.config;

import com.example.civicpulse.model.Complaint;
import com.example.civicpulse.model.User;
import com.example.civicpulse.repository.ComplaintRepository;
import com.example.civicpulse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final ComplaintRepository complaintRepository;

    @Autowired
    public DataInitializer(UserService userService, ComplaintRepository complaintRepository) {
        this.userService = userService;
        this.complaintRepository = complaintRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed System Admin
        if (userService.findByEmail("admin@civicpulse.gov") == null) {
            User admin = new User("System Administrator", "admin@civicpulse.gov", "adminpassword", "adminpassword");
            admin.setRole("ADMIN");
            userService.register(admin);
            System.out.println(">>> Database seeded with Admin user: admin@civicpulse.gov");
        }

        // 2. Seed Citizen Users & Complaints
        if (userService.findByEmail("citizen@example.com") == null) {
            
            // Create default citizen (Jane)
            User citizen = new User("Jane Doe", "citizen@example.com", "password123", "password123");
            citizen.setRole("USER");
            userService.register(citizen);

            // Create second citizen (John)
            User citizen2 = new User("John Smith", "john@example.com", "password123", "password123");
            citizen2.setRole("USER");
            userService.register(citizen2);

            // Fetch persisted citizen entities
            User savedJane = userService.findByEmail("citizen@example.com");
            User savedJohn = userService.findByEmail("john@example.com");

            // Seed complaints with location, department, and remarks
            Complaint c1 = new Complaint(
                "CP-1024", 
                "Pothole / Road Repair", 
                "Pending", 
                "Huge pothole on Maple Street", 
                "A deep pothole has appeared near the main crossing, causing severe traffic delays.", 
                "Maple Street Intersection",
                "Road Maintenance Department",
                "Awaiting road crew scheduling.",
                savedJane
            );
            // John upvotes Maple St pothole
            c1.getSupportingUsers().add(savedJohn);
            complaintRepository.save(c1);

            Complaint c2 = new Complaint(
                "CP-1025", 
                "Streetlight Malfunction", 
                "Resolved", 
                "Dead streetlight bulb on 5th Ave", 
                "The streetlight lamp outside house 14 has been out for three days.", 
                "5th Avenue, Streetlight #14",
                "Electricity Board",
                "Replaced the faulty bulb with a new LED bulb.",
                savedJane
            );
            complaintRepository.save(c2);

            Complaint c3 = new Complaint(
                "CP-1026", 
                "Garbage Collection", 
                "In Progress", 
                "Uncollected trash bags in Central Park", 
                "Trash cans in the children's park area are overflowing with garbage bags.", 
                "Central Park Kids Play Area",
                "Waste Management",
                "Truck dispatched to empty all overflow containers.",
                savedJane
            );
            // Both Jane and John upvote park garbage
            c3.getSupportingUsers().add(savedJane);
            c3.getSupportingUsers().add(savedJohn);
            complaintRepository.save(c3);

            Complaint c4 = new Complaint(
                "CP-1027", 
                "Water Leakage", 
                "Resolved", 
                "Burst pipe on Lincoln Road", 
                "Fresh water is leaking onto the sidewalk from a metal pipe.", 
                "Lincoln Road Sidewalk",
                "Water Supply Department",
                "Patched and sealed main valve line.",
                savedJane
            );
            complaintRepository.save(c4);

            Complaint c5 = new Complaint(
                "CP-1028", 
                "Illegal Parking", 
                "Pending", 
                "Cars blocking fire hydrant on Pine St", 
                "Multiple commercial trucks are regularly parked directly in front of the fire hydrant.", 
                "Pine Street Hydrant Zone",
                "Traffic Control",
                "Notified parking enforcement division.",
                savedJane
            );
            // John upvotes hydrant parking
            c5.getSupportingUsers().add(savedJohn);
            complaintRepository.save(c5);

            System.out.println(">>> Database seeded with Citizen users and 5 upvote-linked complaints.");
        }
    }
}
