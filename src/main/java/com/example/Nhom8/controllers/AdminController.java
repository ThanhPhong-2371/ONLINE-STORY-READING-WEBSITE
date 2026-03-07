package com.example.Nhom8.controllers;

import com.example.Nhom8.models.Role;
import com.example.Nhom8.models.User;
import com.example.Nhom8.repository.RoleRepository;
import com.example.Nhom8.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.example.Nhom8.repository.TransactionRepository transactionRepository;

    @GetMapping("/analytics")
    public ResponseEntity<com.example.Nhom8.dto.AdminAnalyticsDTO> getAnalytics() {
        long totalUsers = userRepository.count();
        long premiumUsers = userRepository.countByIsPremium(true);
        java.math.BigDecimal totalRevenue = transactionRepository.getTotalRevenue();
        if (totalRevenue == null)
            totalRevenue = java.math.BigDecimal.ZERO;

        long success = transactionRepository
                .countByStatus(com.example.Nhom8.models.Transaction.TransactionStatus.SUCCESS);
        long failed = transactionRepository
                .countByStatus(com.example.Nhom8.models.Transaction.TransactionStatus.FAILED);
        long pending = transactionRepository
                .countByStatus(com.example.Nhom8.models.Transaction.TransactionStatus.PENDING);

        List<com.example.Nhom8.dto.RecentTransactionDTO> recentDTOs = transactionRepository
                .findTop10ByOrderByCreatedAtDesc().stream()
                .map(t -> com.example.Nhom8.dto.RecentTransactionDTO.builder()
                        .id(t.getId())
                        .transactionId(t.getTransactionId())
                        .username(t.getUser().getUsername())
                        .packageName(t.getPremiumPackage() != null ? t.getPremiumPackage().getName() : "N/A")
                        .amount(t.getAmount())
                        .paymentMethod(t.getPaymentMethod())
                        .status(t.getStatus())
                        .createdAt(t.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        java.util.Map<String, java.math.BigDecimal> revenueByMethod = new java.util.HashMap<>();
        transactionRepository.getRevenueByPaymentMethod().forEach(objs -> {
            revenueByMethod.put((String) objs[0], (java.math.BigDecimal) objs[1]);
        });

        com.example.Nhom8.dto.AdminAnalyticsDTO analytics = com.example.Nhom8.dto.AdminAnalyticsDTO.builder()
                .totalUsers(totalUsers)
                .totalPremiumUsers(premiumUsers)
                .totalRevenue(totalRevenue)
                .totalTransactions(success + failed + pending)
                .successTransactions(success)
                .failedTransactions(failed)
                .pendingTransactions(pending)
                .recentTransactions(recentDTOs)
                .revenueByMethod(revenueByMethod)
                .build();

        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String username) {
        if (username != null && !username.isEmpty()) {
            return ResponseEntity.ok(userRepository.findByUsernameContainingIgnoreCase(username));
        }
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(!user.isEnabled());
        user.setActive(user.isEnabled()); // Sync both fields if necessary, but isEnabled is used for security
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<?> updateUserRoles(@PathVariable Long userId, @RequestBody Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> roles = roleRepository.findAllById(roleIds).stream().collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("Roles updated successfully");
    }
}
