package com.geoconnect.backend.service;

import java.util.List;

import com.geoconnect.backend.dto.AdminStatsResponse;
import com.geoconnect.backend.dto.ServiceResponse;
import com.geoconnect.backend.dto.UserResponse;

public interface AdminService {

    AdminStatsResponse getPlatformStats();

    List<UserResponse> getAllUsers();

    List<UserResponse> getAllCustomers();

    List<UserResponse> getAllProviders();

    UserResponse getUserById(Long userId);

    UserResponse changeUserRole(Long userId, String role);

    void deleteUser(Long userId);

    List<ServiceResponse> getAllServices();

    ServiceResponse toggleServiceAvailability(Long serviceId);

    void deleteService(Long serviceId);
}