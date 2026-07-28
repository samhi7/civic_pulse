package com.example.civicpulse.controller;

import com.example.civicpulse.model.AuditLog;
import com.example.civicpulse.model.Complaint;
import com.example.civicpulse.model.User;
import com.example.civicpulse.service.ComplaintService;
import com.example.civicpulse.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class MainController {

    private final UserService userService;
    private final ComplaintService complaintService;

    @Autowired
    public MainController(UserService userService, ComplaintService complaintService) {
        this.userService = userService;
        this.complaintService = complaintService;
    }

    // ==========================================
    // 1. PUBLIC WEBSITE & LANDING
    // ==========================================
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // ==========================================
    // 2. AUTHENTICATION FLOW
    // ==========================================
    @GetMapping("/login")
    public String loginPage(Model model,
            @RequestParam(value = "registered", required = false) String registered,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "success", required = false) String success) {
        model.addAttribute("user", new User());
        if (registered != null && registered.equals("true")) {
            model.addAttribute("success", "Registration successful! Proceed to Login.");
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }
        return "login";
    }

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

        // Store session variables
        session.setAttribute("loggedInEmail", authenticated.getEmail());
        session.setAttribute("loggedInName", authenticated.getFullName());
        session.setAttribute("loggedInRole", authenticated.getRole());

        // Redirect based on role and onboarding status
        if ("ADMIN".equals(authenticated.getRole())) {
            return "redirect:/admin/dashboard";
        } else if ("OFFICER".equals(authenticated.getRole())) {
            return "redirect:/officer/dashboard";
        } else {
            if (authenticated.isFirstTime()) {
                return "redirect:/welcome";
            }
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            user.setRole("USER");
            userService.register(user);
            return "redirect:/registration-success?email=" + user.getEmail();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/registration-success")
    public String registrationSuccess(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "registration_success";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email) {
        return "redirect:/verify-otp?email=" + email;
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "otp_verification";
    }

    @PostMapping("/verify-otp")
    public String handleVerifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        return "redirect:/reset-password?email=" + email;
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "password_reset";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("email") String email, @RequestParam("password") String password) {
        User user = userService.findByEmail(email);
        if (user != null) {
            user.setPassword(password);
            userService.updateUserProfile(user);
        }
        return "redirect:/login?success=Password reset successfully. Please login.";
    }

    // ==========================================
    // 3. FIRST-TIME ONBOARDING WELCOME WIZARD
    // ==========================================
    @GetMapping("/welcome")
    public String welcomePage(HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";
        return "welcome";
    }

    @GetMapping("/onboarding/city")
    public String chooseCityPage(HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";
        return "choose_city";
    }

    @PostMapping("/onboarding/city")
    public String saveCity(@RequestParam("city") String city, HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user != null) {
            user.setCity(city);
            userService.updateUserProfile(user);
        }
        return "redirect:/onboarding/locality";
    }

    @GetMapping("/onboarding/locality")
    public String chooseLocalityPage(HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";
        return "choose_locality";
    }

    @PostMapping("/onboarding/locality")
    public String saveLocality(@RequestParam("locality") String locality, HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user != null) {
            user.setLocality(locality);
            userService.updateUserProfile(user);
        }
        return "redirect:/onboarding/profile";
    }

    @GetMapping("/onboarding/profile")
    public String completeProfilePage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user == null)
            return "redirect:/login";
        model.addAttribute("avatarChar", user.getFullName().substring(0, 1).toUpperCase());
        return "complete_profile";
    }

    @PostMapping("/onboarding/profile")
    public String saveProfile(@RequestParam("phone") String phone, @RequestParam("avatarChar") String avatarChar,
            HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user != null) {
            user.setPhone(phone);
            user.setAvatarChar(avatarChar);
            user.setFirstTime(false); // Onboarding complete
            userService.updateUserProfile(user);
        }
        return "redirect:/dashboard";
    }

    // ==========================================
    // 4. CITIZEN CONSOLE (DASHBOARDS & VIEWS)
    // ==========================================
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(value = "tab", required = false) String tab, HttpSession session,
            Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"USER".equals(role)) {
            return "redirect:/login?error=Access Denied";
        }

        User user = userService.findByEmail(email);
        if (user == null)
            return "redirect:/logout";

        String activeTab = (tab != null && !tab.isEmpty()) ? tab : "reports";
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("currentUserEmail", email);

        model.addAttribute("username", user.getFullName());
        model.addAttribute("totalComplaints", complaintService.getTotalCount(user));
        model.addAttribute("pendingComplaints", complaintService.getPendingCount(user));
        model.addAttribute("resolvedComplaints", complaintService.getResolvedCount(user));

        model.addAttribute("complaints", complaintService.getComplaintsByUser(user));
        model.addAttribute("communityComplaints", complaintService.getAllComplaints());

        return "dashboard";
    }

    @GetMapping("/explore")
    public String explorePage(HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";
        return "explore";
    }

    @GetMapping("/my-complaints")
    public String myComplaintsPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user == null)
            return "redirect:/login";
        model.addAttribute("complaints", complaintService.getComplaintsByUser(user));
        return "my_complaints";
    }

    @GetMapping("/report")
    public String reportWizardPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";
        model.addAttribute("complaint", new Complaint());
        return "report_wizard";
    }

    @PostMapping("/report")
    public String fileComplaint(@ModelAttribute("complaint") Complaint complaint,
            @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles,
            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
            HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user == null)
            return "redirect:/login";

        // Save evidence files
        if (imageFiles != null) {
            int imgIndex = 1;
            for (MultipartFile file : imageFiles) {
                if (file != null && !file.isEmpty()) {
                    String url = saveUploadedFile(file);
                    if (url != null) {
                        if (imgIndex == 1)
                            complaint.setImage1(url);
                        else if (imgIndex == 2)
                            complaint.setImage2(url);
                        else if (imgIndex == 3)
                            complaint.setImage3(url);
                        else if (imgIndex == 4)
                            complaint.setImage4(url);
                        else if (imgIndex == 5)
                            complaint.setImage5(url);
                        imgIndex++;
                    }
                }
            }
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoUrl = saveUploadedFile(videoFile);
            if (videoUrl != null) {
                complaint.setVideo(videoUrl);
            }
        }

        Complaint saved = complaintService.fileComplaint(complaint, user);
        return "redirect:/report/success/" + saved.getId();
    }

    // Helper method to process file uploads to static directories
    private String saveUploadedFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String originalFileName = file.getOriginalFilename();
            String cleanFileName = System.currentTimeMillis() + "_"
                    + (originalFileName != null ? originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_") : "upload");

            // Paths
            String baseDir = System.getProperty("user.dir");
            Path srcPath = Paths.get(baseDir, "src", "main", "resources", "static", "uploads");
            Path targetPath = Paths.get(baseDir, "target", "classes", "static", "uploads");

            // Create directories
            Files.createDirectories(srcPath);
            Files.createDirectories(targetPath);

            // Save to src
            Path srcFilePath = srcPath.resolve(cleanFileName);
            Files.write(srcFilePath, file.getBytes());

            // Save to target
            Path targetFilePath = targetPath.resolve(cleanFileName);
            Files.write(targetFilePath, file.getBytes());

            return "/uploads/" + cleanFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/report/success/{id}")
    public String reportSuccessPage(@PathVariable("id") Long id, Model model) {
        Complaint complaint = complaintService.getComplaintById(id);
        if (complaint == null)
            return "redirect:/dashboard";
        model.addAttribute("complaint", complaint);
        return "report_success";
    }

    @GetMapping("/complaints/details/{id}")
    public String complaintDetailsPage(@PathVariable("id") Long id, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";

        Complaint complaint = complaintService.getComplaintById(id);
        if (complaint == null)
            return "redirect:/dashboard";

        model.addAttribute("complaint", complaint);
        model.addAttribute("currentUserEmail", email);
        return "complaint_details";
    }

    @PostMapping("/complaints/rate/{id}")
    public String rateComplaint(@PathVariable("id") Long id, @RequestParam("rating") Integer rating,
            @RequestParam("feedback") String feedback) {
        complaintService.submitRating(id, rating, feedback);
        return "redirect:/complaints/details/" + id;
    }

    @PostMapping("/complaints/comment/{id}")
    public String addComment(@PathVariable("id") Long id, @RequestParam("commentText") String commentText,
            HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user != null) {
            complaintService.addComment(id, commentText, user);
        }
        return "redirect:/complaints/details/" + id;
    }

    @GetMapping("/complaints/support/{id}")
    public String supportComplaint(@PathVariable("id") Long id, HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user != null) {
            complaintService.supportComplaint(id, user);
        }
        return "redirect:/community";
    }

    @GetMapping("/map")
    public String cityMapPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";
        model.addAttribute("complaints", complaintService.getAllComplaints());
        return "city_map";
    }

    @GetMapping("/community")
    public String communityPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";

        model.addAttribute("currentUserEmail", email);
        model.addAttribute("communityComplaints", complaintService.getAllComplaints());
        return "community";
    }

    @GetMapping("/notifications")
    public String notificationsPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";

        model.addAttribute("auditLogs", complaintService.getAuditLogs());
        return "notifications";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        User user = userService.findByEmail(email);
        if (user == null)
            return "redirect:/login";

        model.addAttribute("username", user.getFullName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("role", user.getRole());
        model.addAttribute("avatarChar", user.getAvatarChar());
        model.addAttribute("city", user.getCity());
        model.addAttribute("locality", user.getLocality());

        model.addAttribute("totalComplaints", complaintService.getTotalCount(user));
        model.addAttribute("pendingComplaints", complaintService.getPendingCount(user));
        model.addAttribute("resolvedComplaints", complaintService.getResolvedCount(user));

        return "profile";
    }

    @GetMapping("/settings")
    public String settingsPage(HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";
        return "settings";
    }

    @GetMapping("/help-center")
    public String helpCenterPage(HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        if (email == null)
            return "redirect:/login";
        return "help_center";
    }

    // ==========================================
    // 5. FIELD OFFICER CONSOLE
    // ==========================================
    @GetMapping("/officer/dashboard")
    public String officerDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"OFFICER".equals(role)) {
            return "redirect:/login?error=Access Denied";
        }

        User officer = userService.findByEmail(email);
        if (officer == null)
            return "redirect:/logout";

        model.addAttribute("username", officer.getFullName());
        model.addAttribute("tasks", complaintService.getComplaintsByOfficer(officer));

        return "officer_dashboard";
    }

    @GetMapping("/officer/update/{id}")
    public String officerUpdatePage(@PathVariable("id") Long id, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"OFFICER".equals(role)) {
            return "redirect:/login?error=Access Denied";
        }

        Complaint complaint = complaintService.getComplaintById(id);
        if (complaint == null)
            return "redirect:/officer/dashboard";

        model.addAttribute("complaint", complaint);
        return "officer_update";
    }

    @PostMapping("/officer/update/{id}")
    public String updateOfficerProgress(@PathVariable("id") Long id,
            @RequestParam("status") String status,
            @RequestParam("remarks") String remarks,
            @RequestParam(value = "afterPhotoUrl", required = false) String afterPhotoUrl,
            HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        User officer = userService.findByEmail(email);
        if (officer != null) {
            complaintService.updateOfficerProgress(id, status, remarks, afterPhotoUrl, officer);
        }
        return "redirect:/officer/dashboard";
    }

    @GetMapping("/officer/profile")
    public String officerProfilePage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"OFFICER".equals(role))
            return "redirect:/login";

        User officer = userService.findByEmail(email);
        if (officer == null)
            return "redirect:/login";

        model.addAttribute("username", officer.getFullName());
        model.addAttribute("email", officer.getEmail());
        model.addAttribute("phone", officer.getPhone());
        model.addAttribute("city", officer.getCity());
        model.addAttribute("locality", officer.getLocality());
        model.addAttribute("avatarChar", officer.getAvatarChar());
        return "officer_profile";
    }

    // ==========================================
    // 6. ADMINISTRATOR CONSOLE
    // ==========================================
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"ADMIN".equals(role)) {
            return "redirect:/login?error=Access Denied";
        }

        model.addAttribute("totalComplaints", complaintService.getGlobalTotalCount());
        model.addAttribute("pendingComplaints", complaintService.getGlobalPendingCount());
        model.addAttribute("resolvedComplaints", complaintService.getGlobalResolvedCount());

        return "admin_dashboard";
    }

    @GetMapping("/admin/complaints")
    public String adminComplaintsPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"ADMIN".equals(role))
            return "redirect:/login";

        model.addAttribute("complaints", complaintService.getAllComplaints());
        return "admin_complaints";
    }

    @GetMapping("/admin/assign/{id}")
    public String adminAssignPage(@PathVariable("id") Long id, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");

        if (email == null || !"ADMIN".equals(role))
            return "redirect:/login";

        Complaint complaint = complaintService.getComplaintById(id);
        if (complaint == null)
            return "redirect:/admin/complaints";

        model.addAttribute("complaint", complaint);
        model.addAttribute("officers", userService.findUsersByRole("OFFICER"));
        return "admin_assign";
    }

    @PostMapping("/admin/assign/{id}")
    public String handleAdminAssign(@PathVariable("id") Long id,
            @RequestParam("officerId") Long officerId,
            @RequestParam("department") String department,
            @RequestParam("remarks") String remarks,
            HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        User admin = userService.findByEmail(email);
        if (admin != null) {
            complaintService.assignOfficer(id, officerId, department, remarks, admin);
        }
        return "redirect:/admin/complaints";
    }

    @GetMapping("/admin/departments")
    public String adminDepartmentsPage(HttpSession session) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");
        if (email == null || !"ADMIN".equals(role))
            return "redirect:/login";
        return "admin_departments";
    }

    @GetMapping("/admin/users")
    public String adminUsersPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");
        if (email == null || !"ADMIN".equals(role))
            return "redirect:/login";

        // Fetch all users to display ledger
        model.addAttribute("usersList", userService.findUsersByRole("USER"));
        return "admin_users";
    }

    @GetMapping("/admin/logs")
    public String adminLogsPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInEmail");
        String role = (String) session.getAttribute("loggedInRole");
        if (email == null || !"ADMIN".equals(role))
            return "redirect:/login";

        model.addAttribute("auditLogs", complaintService.getAuditLogs());
        return "admin_logs";
    }

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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/?loggedOut=true";
    }
}
