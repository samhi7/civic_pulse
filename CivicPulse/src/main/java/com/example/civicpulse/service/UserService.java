package com.example.civicpulse.service;

import com.example.civicpulse.model.User;
import com.example.civicpulse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Register a new user
    public User register(User user) throws Exception {
        // Validate email uniqueness
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        // Validate passwords match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        // Hash password
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    // 2. Authenticate user
    public User authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        String hashedPassword = hashPassword(password);
        if (user.getPassword().equals(hashedPassword)) {
            return user;
        }
        
        return null;
    }

    // 3. Find User by Email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // SHA-256 hashing utility
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error hashing password", ex);
        }
    }
}
