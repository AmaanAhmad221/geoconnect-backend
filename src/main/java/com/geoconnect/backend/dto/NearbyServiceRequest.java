package com.geoconnect.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NearbyServiceRequest {

	@NotNull(message = "Latitude is required")
	private Double latitude;

	@NotNull(message = "Longitude is required")
	private Double longitude;

	// Default radius = 10 KM
	private Double radiusKm = 10.0;

	private String category;
}