package com.geoconnect.backend.dto;

import com.geoconnect.backend.entity.ServiceCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ServiceRequest {

	@NotBlank(message = "Title is required")
	private String title;

	@NotBlank(message = "Description is required")
	private String description;

	@NotNull(message = "Price is required")
	@Positive(message = "Price must be positive")
	private Double price;

	@NotNull(message = "Category is required")
	private ServiceCategory category;

	@NotBlank(message = "City is required")
	private String city;

	@NotBlank(message = "Area is required")
	private String area;
	
	private Double latitude;
	private Double longitude;
}
