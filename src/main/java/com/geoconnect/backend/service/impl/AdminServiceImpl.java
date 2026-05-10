package com.geoconnect.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.geoconnect.backend.dto.AdminStatsResponse;
import com.geoconnect.backend.dto.ServiceResponse;
import com.geoconnect.backend.dto.UserResponse;
import com.geoconnect.backend.entity.BookingStatus;
import com.geoconnect.backend.entity.Role;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.exception.BadRequestException;
import com.geoconnect.backend.exception.ResourceNotFoundException;
import com.geoconnect.backend.repository.BookingRepository;
import com.geoconnect.backend.repository.ReviewRepository;
import com.geoconnect.backend.repository.ServiceRepository;
import com.geoconnect.backend.repository.UserRepository;
import com.geoconnect.backend.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final ServiceRepository serviceRepository;
	private final BookingRepository bookingRepository;
	private final ReviewRepository reviewRepository;

	@Override
	public AdminStatsResponse getPlatformStats() {
		long totalUsers = userRepository.count();
		long totalCustomers = userRepository.countByRole(Role.CUSTOMER);
		long totalProviders = userRepository.countByRole(Role.PROVIDER);
		long totalServices = serviceRepository.count();
		long totalBookings = bookingRepository.count();
		long totalReviews = reviewRepository.count();
		long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);
		long completedBookings = bookingRepository.countByStatus(BookingStatus.COMPLETED);

		return new AdminStatsResponse(totalUsers, totalCustomers, totalProviders, totalServices, totalBookings,
				totalReviews, pendingBookings, completedBookings);
	}

	@Override
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(UserResponse::fromUser).collect(Collectors.toList());
	}

	@Override
	public List<UserResponse> getAllCustomers() {
		return userRepository.findByRole(Role.CUSTOMER).stream().map(UserResponse::fromUser)
				.collect(Collectors.toList());
	}

	@Override
	public List<UserResponse> getAllProviders() {
		return userRepository.findByRole(Role.PROVIDER).stream().map(UserResponse::fromUser)
				.collect(Collectors.toList());
	}

	@Override
	public UserResponse getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		return UserResponse.fromUser(user);
	}

	@Override
	public UserResponse changeUserRole(Long userId, String role) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		try {
			Role newRole = Role.valueOf(role.toUpperCase());
			user.setRole(newRole);
			return UserResponse.fromUser(userRepository.save(user));
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Invalid role! Valid roles: CUSTOMER, PROVIDER, ADMIN");
		}
	}

	@Override
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		userRepository.delete(user);
	}

	@Override
	public List<ServiceResponse> getAllServices() {
		return serviceRepository.findAll().stream().map(ServiceResponse::fromService).collect(Collectors.toList());
	}

	@Override
	public ServiceResponse toggleServiceAvailability(Long serviceId) {
		com.geoconnect.backend.entity.Service service = serviceRepository.findById(serviceId)
				.orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));
		service.setAvailable(!service.isAvailable());
		return ServiceResponse.fromService(serviceRepository.save(service));
	}

	@Override
	public void deleteService(Long serviceId) {
		com.geoconnect.backend.entity.Service service = serviceRepository.findById(serviceId)
				.orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));
		serviceRepository.delete(service);
	}
}