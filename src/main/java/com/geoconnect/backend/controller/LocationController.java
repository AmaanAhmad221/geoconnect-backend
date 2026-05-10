package com.geoconnect.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.LocationUpdateRequest;
import com.geoconnect.backend.dto.NearbyServiceRequest;
import com.geoconnect.backend.dto.NearbyServiceResponse;
import com.geoconnect.backend.entity.Service;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.repository.ServiceRepository;
import com.geoconnect.backend.repository.UserRepository;
import com.geoconnect.backend.util.LocationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
@Tag(name = "Location", description = "Location and nearby search APIs")
public class LocationController {

	private final UserRepository userRepository;
	private final ServiceRepository serviceRepository;
	private final LocationUtils locationUtils;

	// Update user's current location
	@PutMapping("/update")
	public ResponseEntity<ApiResponse<String>> updateLocation(@Valid @RequestBody LocationUpdateRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {

		User user = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found!"));

		user.setLatitude(request.getLatitude());
		user.setLongitude(request.getLongitude());
		user.setCity(request.getCity());
		user.setAddress(request.getAddress());
		userRepository.save(user);

		return ResponseEntity.ok(ApiResponse.success("Location updated successfully!", null));
	}

	// Find nearby services
	@PostMapping("/nearby-services")
	public ResponseEntity<ApiResponse<List<NearbyServiceResponse>>> findNearbyServices(
			@Valid @RequestBody NearbyServiceRequest request) {

		// Get all available services
		List<Service> allServices = serviceRepository.findByAvailableTrue();

		// Filter by distance and map to response
		List<NearbyServiceResponse> nearbyServices = allServices
				.stream().filter(
						service -> service.getLatitude() != null && service.getLongitude() != null
								&& locationUtils.isWithinRadius(request.getLatitude(), request.getLongitude(),
										service.getLatitude(), service.getLongitude(), request.getRadiusKm()))
				.map(service -> {
					NearbyServiceResponse response = new NearbyServiceResponse();
					response.setId(service.getId());
					response.setTitle(service.getTitle());
					response.setDescription(service.getDescription());
					response.setPrice(service.getPrice());
					response.setCategory(service.getCategory());
					response.setCity(service.getCity());
					response.setArea(service.getArea());
					response.setLatitude(service.getLatitude());
					response.setLongitude(service.getLongitude());
					response.setAvailable(service.isAvailable());
					response.setProviderName(service.getProvider().getName());
					response.setProviderUsername(service.getProvider().getUsername());

					// Calculate exact distance
					double distance = locationUtils.calculateDistance(request.getLatitude(), request.getLongitude(),
							service.getLatitude(), service.getLongitude());
					// Round to 2 decimal places
					response.setDistanceKm(Math.round(distance * 100.0) / 100.0);

					return response;
				}).sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm())).collect(Collectors.toList());

		return ResponseEntity
				.ok(ApiResponse.success("Found " + nearbyServices.size() + " services nearby!", nearbyServices));
	}
}