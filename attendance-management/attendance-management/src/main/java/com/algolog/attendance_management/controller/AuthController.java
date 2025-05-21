package com.algolog.attendance_management.controller;

import com.algolog.attendance_management.configue.JwtUtil;
import com.algolog.attendance_management.dtos.LoginRequest;
import com.algolog.attendance_management.dtos.RequestWrapper;
import com.algolog.attendance_management.dtos.requestDtos.RegisterUserRequest;
import com.algolog.attendance_management.dtos.resposedtos.JwtResponse;
import com.algolog.attendance_management.entity.Role;
import com.algolog.attendance_management.entity.User;
import com.algolog.attendance_management.repository.UserRepository;
import com.algolog.attendance_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RequestWrapper<RegisterUserRequest> requestWrapper) {
        List<RegisterUserRequest> users = requestWrapper.getPayLoad();

        for (RegisterUserRequest req : users) {
            if (userRepository.existsByUsername(req.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body("Username already exists: " + req.getUsername());
            }

            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setRole(Role.valueOf(req.getRole())); // Make sure Role enum matches string
            user.setEmail(req.getEmail());
            user.setMobile(req.getMobile());
            userRepository.save(user);
        }

        return ResponseEntity.ok("Users registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            String token = jwtUtil.generateToken(loginRequest.getUsername());

            return ResponseEntity.ok(new JwtResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(401)
                    .body("Invalid username or password.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(Principal principal) {
        return ResponseEntity.ok("Logout successful for user: " + (principal != null ? principal.getName() : "Unknown"));
    }
}
