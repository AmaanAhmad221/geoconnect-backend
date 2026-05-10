package com.geoconnect.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoconnect.backend.dto.AdminStatsResponse;
import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.ServiceResponse;
import com.geoconnect.backend.dto.UserResponse;
import com.geoconnect.backend.service.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {

    private final AdminService adminService;

    // Platform statistics
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getPlatformStats() {
        return ResponseEntity.ok(
            ApiResponse.success("Stats fetched!",
                adminService.getPlatformStats())
        );
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(
            ApiResponse.success("Users fetched!",
                adminService.getAllUsers())
        );
    }

    // Get all customers
    @GetMapping("/users/customers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllCustomers() {
        return ResponseEntity.ok(
            ApiResponse.success("Customers fetched!",
                adminService.getAllCustomers())
        );
    }

    // Get all providers
    @GetMapping("/users/providers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllProviders() {
        return ResponseEntity.ok(
            ApiResponse.success("Providers fetched!",
                adminService.getAllProviders())
        );
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("User fetched!",
                adminService.getUserById(id))
        );
    }

    // Change user role
    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserResponse>> changeUserRole(
            @PathVariable Long id,
            @RequestParam String role) {
        return ResponseEntity.ok(
            ApiResponse.success("Role updated!",
                adminService.changeUserRole(id, role))
        );
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(
            ApiResponse.success("User deleted!", null)
        );
    }

    // Get all services
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAllServices() {
        return ResponseEntity.ok(
            ApiResponse.success("Services fetched!",
                adminService.getAllServices())
        );
    }

    // Toggle service availability
    @PutMapping("/services/{id}/toggle")
    public ResponseEntity<ApiResponse<ServiceResponse>> toggleService(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("Service availability toggled!",
                adminService.toggleServiceAvailability(id))
        );
    }

    // Delete service
    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<String>> deleteService(
            @PathVariable Long id) {
        adminService.deleteService(id);
        return ResponseEntity.ok(
            ApiResponse.success("Service deleted!", null)
        );
    }
}