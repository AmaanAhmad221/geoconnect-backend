package com.geoconnect.backend.service;

import java.util.List;

import com.geoconnect.backend.dto.ProviderRatingResponse;
import com.geoconnect.backend.dto.ReviewRequest;
import com.geoconnect.backend.dto.ReviewResponse;

public interface ReviewService {

	ReviewResponse createReview(ReviewRequest request, String username);

	List<ReviewResponse> getProviderReviews(String providerUsername);

	List<ReviewResponse> getMyReviews(String username);

	ProviderRatingResponse getProviderRating(String providerUsername);
}
