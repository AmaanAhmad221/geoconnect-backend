package com.geoconnect.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.AuthResponse;
import com.geoconnect.backend.dto.LoginRequest;
import com.geoconnect.backend.dto.RegisterRequest;
import com.geoconnect.backend.dto.UserResponse;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register and Login APIs")
public class AuthController {

	private final UserService userService;

	@PostMapping("/register")
	@Operation(summary = "Register a new user", description = "Creates a new user account with CUSTOMER role")
	public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {

		User user = userService.registerUser(request);
		UserResponse userResponse = UserResponse.fromUser(user);

		return ResponseEntity.ok(ApiResponse.success("User registered successfully!", userResponse));
	}

	@PostMapping("/login")
	@Operation(summary = "Login user", description = "Login with username/email and password, returns JWT token")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
		AuthResponse authResponse = userService.login(request);
		return ResponseEntity.ok(ApiResponse.success("Login successful!", authResponse));
	}
}