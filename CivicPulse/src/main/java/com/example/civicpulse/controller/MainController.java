package com.example.civicpulse.controller;

import com.example.civicpulse.model.Complaint;
import com.example.civicpulse.model.User;
import com.example.civicpulse.service.ComplaintService;
import com.example.civicpulse.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final UserService userService;
    private final ComplaintService complaintService;

    @Autowired
    public MainController(UserService userService, ComplaintService complaintService) {
        this.userService = userService;
        this.complaintService = complaintService;
    }

    // 1. Home Page
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // 2. Register Page (GET)
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // 2. Register Page (POST) - Database Registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            user.setRole("USER");
            userService.register(user);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return "register";
        }
    }

    // 3. Login Page (GET)
    @GetMapping("/login")
    public String loginPage(Model model, String registered, String error) {
        model.addAttribute("user", new User());
        if (registered != null && registered.equals("true")) {
            model.addAttribute("success", "Registration successful! Please login.");
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    // 3. Login Page (POST) - Database Authentication & Role-Based Routing
    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, HttpSession session, Model model) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || 
            user.getPassword() == null || user.getPassword().isEmpty()) {
            model.addAttribute("error", "Email and Password are required");
            return "login";
        }

        User authenticated = userService.authenticate(user.getEmail(), user.getPassword());
        if (authenticated == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        // Store user details in session
        session.setAttribute("loggedInEmail", authenticated.getEmail());
        session.setAttribute("loggedInName", authenticated.getFullName());
        session.setAttribute("loggedInRole", authenticated.getRole());

        // Redirect based on role
        if ("ADMIN".equals(authenticated.getRole())) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/dashboard";
        }
    }

    // 4. Citizen Dashboard Page (GET) - Dynamic Database Metrics & Tabs
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(value = "tab", required = false) String tab, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");
        
        if (email == null || !"USER".equals(role)) {
            return "redirect:/login?error=Please login to view your dashboard";
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/logout";
        }

        // Determine active tab state
        String activeTab = (tab != null && !tab.isEmpty()) ? tab : "reports";
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("currentUserEmail", email);

        model.addAttribute("username", user.getFullName());
        model.addAttribute("totalComplaints", complaintService.getTotalCount(user));
        model.addAttribute("pendingComplaints", complaintService.getPendingCount(user));
        model.addAttribute("resolvedComplaints", complaintService.getResolvedCount(user));
        
        // Personal list of complaints (Newest first)
        model.addAttribute("complaints", complaintService.getComplaintsByUser(user));
        
        // Global community list of complaints (Newest first)
        model.addAttribute("communityComplaints", complaintService.getAllComplaints());

        return "dashboard";
    }

    // 5. Admin Dashboard Page (GET) - Resolution Console
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"ADMIN".equals(role)) {
            return "redirect:/login?error=Please login as Administrator";
        }

        model.addAttribute("username", session.getAttribute("loggedInName"));
        model.addAttribute("totalComplaints", complaintService.getGlobalTotalCount());
        model.addAttribute("pendingComplaints", complaintService.getGlobalPendingCount());
        model.addAttribute("resolvedComplaints", complaintService.getGlobalResolvedCount());
        model.addAttribute("complaints", complaintService.getAllComplaints());

        return "admin_dashboard";
    }

    // 6. Admin Action: Load Update Complaint Screen (GET)
    @GetMapping("/admin/update/{id}")
    public String updateComplaintPage(@PathVariable("id") Long id, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"ADMIN".equals(role)) {
            return "redirect:/login?error=Please login as Administrator";
        }

        Complaint complaint = complaintService.getComplaintById(id);
        if (complaint == null) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("complaint", complaint);
        return "admin_update";
    }

    // 6. Admin Action: Save Update Complaint Details (POST)
    @PostMapping("/admin/update/{id}")
    public String updateComplaint(@PathVariable("id") Long id,
                                  @RequestParam("status") String status,
                                  @RequestParam("department") String department,
                                  @RequestParam("adminRemarks") String adminRemarks,
                                  HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"ADMIN".equals(role)) {
            return "redirect:/login?error=Unauthorized action";
        }

        complaintService.updateComplaint(id, status, department, adminRemarks);
        return "redirect:/admin/dashboard";
    }

    // 7. Citizen Action: Upvote / Support Complaint (GET)
    @GetMapping("/complaints/support/{id}")
    public String supportComplaint(@PathVariable("id") Long id, HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"USER".equals(role)) {
            return "redirect:/login?error=Please login to support issues";
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/logout";
        }

        complaintService.supportComplaint(id, user);
        return "redirect:/dashboard?tab=community";
    }

    // 8. Submit New Complaint (GET)
    @GetMapping("/report")
    public String reportComplaintPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"USER".equals(role)) {
            return "redirect:/login?error=Please login to report a complaint";
        }

        model.addAttribute("complaint", new Complaint());
        return "report";
    }

    // 8. Submit New Complaint (POST)
    @PostMapping("/report")
    public String fileComplaint(@ModelAttribute("complaint") Complaint complaint, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"USER".equals(role)) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/logout";
        }

        if (complaint.getCategory() == null || complaint.getCategory().isEmpty() ||
            complaint.getTitle() == null || complaint.getTitle().isEmpty() ||
            complaint.getLocation() == null || complaint.getLocation().isEmpty()) {
            model.addAttribute("error", "Category, Title, and Location are required");
            return "report";
        }

        complaintService.fileComplaint(complaint, user);
        return "redirect:/dashboard";
    }

    // 9. Profile Page (GET)
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null) {
            return "redirect:/login?error=Please login to view your profile";
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/logout";
        }

        model.addAttribute("username", user.getFullName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("role", user.getRole());

        if ("USER".equals(user.getRole())) {
            model.addAttribute("totalComplaints", complaintService.getTotalCount(user));
            model.addAttribute("pendingComplaints", complaintService.getPendingCount(user));
            model.addAttribute("resolvedComplaints", complaintService.getResolvedCount(user));
        }

        return "profile";
    }

    // 10. FAQ & Help Page (GET)
    @GetMapping("/faq")
    public String faqPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email != null) {
            model.addAttribute("role", role);
            model.addAttribute("username", session.getAttribute("loggedInName"));
        }
        return "faq";
    }

    // 11. Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/?loggedOut=true";
    }
}
