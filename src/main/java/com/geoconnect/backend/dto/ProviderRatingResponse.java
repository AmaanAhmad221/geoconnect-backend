package com.geoconnect.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProviderRatingResponse {

	private String providerUsername;
	private String providerName;
	private double averageRating;
	private long totalReviews;
}
