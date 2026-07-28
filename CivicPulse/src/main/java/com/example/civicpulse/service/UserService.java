package com.example.civicpulse.service;

import com.example.civicpulse.model.User;
import com.example.civicpulse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Register a new user
    public User register(User user) {
        // Validation checks
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full Name is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (!user.getPassword().equals(user.getConfirmPassword()) && user.getId() == null) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Email uniqueness check
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Set default onboarding avatar character
        user.setAvatarChar(user.getFullName().substring(0, 1).toUpperCase());

        return userRepository.save(user);
    }

    // 2. Authenticate user logins
    public User authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    // 3. Find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // 4. Update existing user profile
    public void updateUserProfile(User user) {
        userRepository.save(user);
    }

    // 5. Find users by role (e.g. OFFICER)
    public List<User> findUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
}
