package com.example.Nhom8.controllers;

import com.example.Nhom8.dto.JwtAuthenticationResponse;
import com.example.Nhom8.dto.LoginRequest;
import com.example.Nhom8.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final com.example.Nhom8.repository.UserRepository userRepository;
    private final com.example.Nhom8.repository.RoleRepository roleRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        com.example.Nhom8.models.User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        java.util.List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user.getUsername(), user.getAvatar(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "avatar", required = false) org.springframework.web.multipart.MultipartFile avatarFile) {

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email Address is already in use!");
        }

        com.example.Nhom8.models.User user = new com.example.Nhom8.models.User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setProvider(com.example.Nhom8.models.User.AuthProvider.LOCAL);

        String avatarPath = "https://via.placeholder.com/150";
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String fileName = org.springframework.util.StringUtils
                        .cleanPath(java.util.UUID.randomUUID().toString() + "_" + avatarFile.getOriginalFilename());
                java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads");
                if (!java.nio.file.Files.exists(uploadPath)) {
                    java.nio.file.Files.createDirectories(uploadPath);
                }
                java.nio.file.Path filePath = uploadPath.resolve(fileName);
                java.nio.file.Files.copy(avatarFile.getInputStream(), filePath,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                avatarPath = "/uploads/" + fileName;
            } catch (java.io.IOException e) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Could not upload avatar image");
            }
        }
        user.setAvatar(avatarPath);

        com.example.Nhom8.models.Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(java.util.Collections.singleton(userRole));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}
