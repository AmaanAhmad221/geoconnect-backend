package com.geoconnect.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoconnect.backend.dto.ApiResponse;

@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping("/user")
	public ResponseEntity<ApiResponse<String>> userAccess() {
		return ResponseEntity.ok(ApiResponse.success("User access granted!", "Welcome User!"));
	}

	@GetMapping("/customer")
	public ResponseEntity<ApiResponse<String>> customerAccess() {
		return ResponseEntity.ok(ApiResponse.success("Customer access granted!", "Welcome Customer!"));
	}

	@GetMapping("/provider")
	@PreAuthorize("hasRole('PROVIDER')")
	public ResponseEntity<ApiResponse<String>> providerAccess() {
		return ResponseEntity.ok(ApiResponse.success("Provider access granted!", "Welcome Provider!"));
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<String>> adminAccess() {
		return ResponseEntity.ok(ApiResponse.success("Admin access granted!", "Welcome Admin!"));
	}

	@GetMapping("/admin-or-provider")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
	public ResponseEntity<ApiResponse<String>> adminOrProviderAccess() {
		return ResponseEntity.ok(ApiResponse.success("Access granted!", "Welcome Admin or Provider!"));
	}
}
