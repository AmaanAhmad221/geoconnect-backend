package com.geoconnect.backend.service.impl;

import com.geoconnect.backend.dto.ProviderRatingResponse;
import com.geoconnect.backend.dto.ReviewRequest;
import com.geoconnect.backend.dto.ReviewResponse;
import com.geoconnect.backend.entity.Booking;
import com.geoconnect.backend.entity.BookingStatus;
import com.geoconnect.backend.entity.Review;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.exception.BadRequestException;
import com.geoconnect.backend.exception.DuplicateResourceException;
import com.geoconnect.backend.exception.ResourceNotFoundException;
import com.geoconnect.backend.exception.UnauthorizedException;
import com.geoconnect.backend.repository.BookingRepository;
import com.geoconnect.backend.repository.ReviewRepository;
import com.geoconnect.backend.repository.UserRepository;
import com.geoconnect.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final BookingRepository bookingRepository;
	private final UserRepository userRepository;

	@Override
	public ReviewResponse createReview(ReviewRequest request, String username) {
		User customer = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		Booking booking = bookingRepository.findById(request.getBookingId())
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "id", request.getBookingId()));

		if (!booking.getCustomer().getUsername().equals(username)) {
			throw new UnauthorizedException("You can only review your own bookings!");
		}

		if (booking.getStatus() != BookingStatus.COMPLETED) {
			throw new BadRequestException("You can only review completed bookings!");
		}

		if (reviewRepository.findByBooking(booking).isPresent()) {
			throw new DuplicateResourceException("You have already reviewed this booking!");
		}

		Review review = new Review();
		review.setCustomer(customer);
		review.setProvider(booking.getProvider());
		review.setBooking(booking);
		review.setService(booking.getBookedService());
		review.setRating(request.getRating());
		review.setComment(request.getComment());

		return ReviewResponse.fromReview(reviewRepository.save(review));
	}

	@Override
	public List<ReviewResponse> getProviderReviews(String providerUsername) {
		User provider = userRepository.findByUsername(providerUsername)
				.orElseThrow(() -> new ResourceNotFoundException("Provider", "username", providerUsername));

		return reviewRepository.findByProvider(provider).stream().map(ReviewResponse::fromReview)
				.collect(Collectors.toList());
	}

	@Override
	public List<ReviewResponse> getMyReviews(String username) {
		User customer = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		return reviewRepository.findByCustomer(customer).stream().map(ReviewResponse::fromReview)
				.collect(Collectors.toList());
	}

	@Override
	public ProviderRatingResponse getProviderRating(String providerUsername) {
		User provider = userRepository.findByUsername(providerUsername)
				.orElseThrow(() -> new ResourceNotFoundException("Provider", "username", providerUsername));

		Double avgRating = reviewRepository.findAverageRatingByProvider(provider);
		long totalReviews = reviewRepository.countByProvider(provider);
		double roundedRating = avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0;

		return new ProviderRatingResponse(provider.getUsername(), provider.getName(), roundedRating, totalReviews);
	}
}