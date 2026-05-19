package com.bekzhanuly.courseplatform.service.impl;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedLoginRequest;
import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedRegisterRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedAuthResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedRole;
import com.bekzhanuly.courseplatform.exception.BekzhanulYNurmukhamedExceptions;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedUserRepository;
import com.bekzhanuly.courseplatform.security.jwt.BekzhanulYNurmukhamedJwtUtil;
import com.bekzhanuly.courseplatform.security.service.BekzhanulYNurmukhamedUserDetails;
import com.bekzhanuly.courseplatform.service.async.BekzhanulYNurmukhamedEmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service - Registration, Login, JWT
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedAuthService {

    private final BekzhanulYNurmukhamedUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BekzhanulYNurmukhamedJwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BekzhanulYNurmukhamedEmailNotificationService emailService;

    @Transactional
    public BekzhanulYNurmukhamedAuthResponse register(BekzhanulYNurmukhamedRegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAlreadyExistsException(
                    "Username already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAlreadyExistsException(
                    "Email already registered: " + request.getEmail());
        }

        BekzhanulYNurmukhamedRole role = request.getRole() != null
                ? request.getRole()
                : BekzhanulYNurmukhamedRole.ROLE_STUDENT;

        BekzhanulYNurmukhamedUser user = BekzhanulYNurmukhamedUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(role)
                .isActive(true)
                .isEmailVerified(false)
                .build();

        BekzhanulYNurmukhamedUser saved = userRepository.save(user);
        log.info("User registered successfully: {} with role {}", saved.getUsername(), saved.getRole());

        // Async welcome email (non-blocking)
        emailService.sendWelcomeEmail(saved.getEmail(), saved.getUsername());

        BekzhanulYNurmukhamedUserDetails userDetails = new BekzhanulYNurmukhamedUserDetails(saved);
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return BekzhanulYNurmukhamedAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpiration())
                .userId(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .role(saved.getRole())
                .build();
    }

    public BekzhanulYNurmukhamedAuthResponse login(BekzhanulYNurmukhamedLoginRequest request) {
        log.info("Login attempt for: {}", request.getUsernameOrEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        BekzhanulYNurmukhamedUserDetails userDetails =
                (BekzhanulYNurmukhamedUserDetails) authentication.getPrincipal();

        BekzhanulYNurmukhamedUser user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found"));

        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        log.info("User logged in successfully: {}", userDetails.getUsername());

        return BekzhanulYNurmukhamedAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpiration())
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
