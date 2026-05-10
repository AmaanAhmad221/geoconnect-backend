package com.geoconnect.backend.dto;

import com.geoconnect.backend.entity.Service;
import com.geoconnect.backend.entity.ServiceCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceResponse {

	private Long id;
	private String title;
	private String description;
	private Double price;
	private ServiceCategory category;
	private String city;
	private String area;
	private Double latitude;
	private Double longitude;
	private boolean available;
	private Long providerId;
	private String providerName;
	private String providerUsername;
	private LocalDateTime createdAt;

	public static ServiceResponse fromService(Service service) {
		ServiceResponse response = new ServiceResponse();
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
		response.setProviderId(service.getProvider().getId());
		response.setProviderName(service.getProvider().getName());
		response.setProviderUsername(service.getProvider().getUsername());
		response.setCreatedAt(service.getCreatedAt());
		return response;
	}
}