package com.company.employeemanagement.controller;

import com.company.employeemanagement.dto.LoginRequest;
import com.company.employeemanagement.dto.LoginResponse;
import com.company.employeemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        boolean valid = userService.validateLogin(
                request.getUsername(),
                request.getPassword()
        );

        if (!valid) {
            return ResponseEntity
                    .status(401)
                    .body(new LoginResponse(
                            "Invalid username or password",
                            null
                    ));
        }

        return ResponseEntity.ok(
                new LoginResponse(
                        "Login successful",
                        request.getUsername()
                )
        );
    }
}