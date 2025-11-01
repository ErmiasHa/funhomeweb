package com.funhomebase.funhomeweb.controller;

import com.funhomebase.funhomeweb.model.User;
import com.funhomebase.funhomeweb.repository.UserRepository;
import com.funhomebase.funhomeweb.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "E-post används redan."));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("CUSTOMER");
        }
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Registrering lyckades!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            var user = userRepository.findByEmail(email).orElseThrow();
            String token = jwtService.generateToken(String.valueOf(user));

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole(),
                    "email", user.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", "Fel e-post eller lösenord."));
        }
    }
}
