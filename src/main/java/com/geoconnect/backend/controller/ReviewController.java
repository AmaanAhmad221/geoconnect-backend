package com.geoconnect.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.ProviderRatingResponse;
import com.geoconnect.backend.dto.ReviewRequest;
import com.geoconnect.backend.dto.ReviewResponse;
import com.geoconnect.backend.service.ReviewService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Reviews and ratings APIs")
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<ReviewResponse>> createReview(@Valid @RequestBody ReviewRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {
		ReviewResponse response = reviewService.createReview(request, userDetails.getUsername());

		return ResponseEntity.ok(ApiResponse.success("Review submitted successfully!", response));
	}

	@GetMapping("/provider/{username}")
	public ResponseEntity<ApiResponse<List<ReviewResponse>>> getProviderReviews(@PathVariable String username) {
		return ResponseEntity.ok(ApiResponse.success("Reviews fetched!", reviewService.getProviderReviews(username)));

	}

	@GetMapping("/my-reviews")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<List<ReviewResponse>>> getMyReviews(
			@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(
				ApiResponse.success("Your Reviews fetched!", reviewService.getMyReviews(userDetails.getUsername())));
	}

	@GetMapping("/rating/{username}")
	public ResponseEntity<ApiResponse<ProviderRatingResponse>> getProviderRating(@PathVariable String username) {
		return ResponseEntity
				.ok(ApiResponse.success("Provider rating fetched!", reviewService.getProviderRating(username)));
	}
}
