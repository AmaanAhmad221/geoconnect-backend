package com.geoconnect.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {

	@NotNull(message = "Service ID is required")
	private Long serviceId;

	private String notes;

	private String address;

	private Double latitude;

	private Double longitude;

	@NotNull(message = "Scheduled time is required")
	private LocalDateTime scheduledAt;
}