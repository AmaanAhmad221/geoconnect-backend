package com.geoconnect.backend.dto;

import com.geoconnect.backend.entity.ServiceCategory;
import lombok.Data;

@Data
public class NearbyServiceResponse {

	private Long id;
	private String title;
	private String description;
	private Double price;
	private ServiceCategory category;
	private String city;
	private String area;
	private Double latitude;
	private Double longitude;
	private String providerName;
	private String providerUsername;
	private Double distanceKm; // Distance from user!
	private boolean available;
}