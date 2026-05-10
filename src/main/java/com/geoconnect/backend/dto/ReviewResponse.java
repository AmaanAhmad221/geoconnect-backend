package com.geoconnect.backend.dto;

import java.time.LocalDateTime;

import com.geoconnect.backend.entity.Review;

import lombok.Data;

@Data
public class ReviewResponse {

	private Long id;
	private Long bookingId;
	private Long serviceId;
	private String serviceTitle;
	private Long customerId;
	private String customerName;
	private String customerUsername;
	private Long providerId;
	private String providerName;
	private String providerUsername;
	private int rating;
	private String comment;
	private LocalDateTime createdAt;

	public static ReviewResponse fromReview(Review review) {
		ReviewResponse response = new ReviewResponse();
		response.setId(review.getId());
		response.setBookingId(review.getBooking().getId());
		response.setBookingId(review.getService().getId());
		response.setServiceTitle(review.getService().getTitle());
		response.setCustomerId(review.getCustomer().getId());
		response.setCustomerName(review.getCustomer().getName());
		response.setCustomerUsername(review.getCustomer().getUsername());
		response.setProviderId(review.getProvider().getId());
		response.setProviderName(review.getProvider().getName());
		response.setProviderUsername(review.getProvider().getUsername());
		response.setRating(review.getRating());
		response.setComment(review.getComment());
		response.setCreatedAt(review.getCreatedAt());
		return response;

	}

}
