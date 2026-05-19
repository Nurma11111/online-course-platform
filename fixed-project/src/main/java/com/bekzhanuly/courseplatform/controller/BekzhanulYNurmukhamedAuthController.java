package com.bekzhanuly.courseplatform.controller;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedLoginRequest;
import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedRegisterRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedApiResponse;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedAuthResponse;
import com.bekzhanuly.courseplatform.service.impl.BekzhanulYNurmukhamedAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller - Register, Login
 * Author: Bekzhanuly Nurmukhamed
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Registration, Login and JWT management")
public class BekzhanulYNurmukhamedAuthController {

    private final BekzhanulYNurmukhamedAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new account and returns JWT tokens")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedAuthResponse>> register(
            @Valid @RequestBody BekzhanulYNurmukhamedRegisterRequest request) {
        log.info("POST /auth/register - username: {}", request.getUsername());
        BekzhanulYNurmukhamedAuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BekzhanulYNurmukhamedApiResponse.success(response, "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate and receive JWT access + refresh tokens")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedAuthResponse>> login(
            @Valid @RequestBody BekzhanulYNurmukhamedLoginRequest request) {
        log.info("POST /auth/login - user: {}", request.getUsernameOrEmail());
        BekzhanulYNurmukhamedAuthResponse response = authService.login(request);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(response, "Login successful"));
    }
}
