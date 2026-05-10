package com.geoconnect.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.BookingRequest;
import com.geoconnect.backend.dto.BookingResponse;
import com.geoconnect.backend.service.BookingService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking management APIs")
public class BookingController {

	private final BookingService bookingService;

	// Customer creates booking
	@PostMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {

		BookingResponse response = bookingService.createBooking(request, userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Booking created successfully!", response));
	}

	// Provider accepts booking
	@PutMapping("/{id}/accept")
	@PreAuthorize("hasRole('PROVIDER')")
	public ResponseEntity<ApiResponse<BookingResponse>> acceptBooking(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		BookingResponse response = bookingService.acceptBooking(id, userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Booking accepted!", response));
	}

	// Provider rejects booking
	@PutMapping("/{id}/reject")
	@PreAuthorize("hasRole('PROVIDER')")
	public ResponseEntity<ApiResponse<BookingResponse>> rejectBooking(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		BookingResponse response = bookingService.rejectBooking(id, userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Booking rejected!", response));
	}

	// Customer cancels booking
	@PutMapping("/{id}/cancel")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		BookingResponse response = bookingService.cancelBooking(id, userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Booking cancelled!", response));
	}

	// Provider completes booking
	@PutMapping("/{id}/complete")
	@PreAuthorize("hasRole('PROVIDER')")
	public ResponseEntity<ApiResponse<BookingResponse>> completeBooking(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		BookingResponse response = bookingService.completeBooking(id, userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Booking completed!", response));
	}

	// Customer views their bookings
	@GetMapping("/my-bookings")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookingsAsCustomer(
			@AuthenticationPrincipal UserDetails userDetails) {

		return ResponseEntity.ok(ApiResponse.success("Bookings fetched!",
				bookingService.getMyBookingsAsCustomer(userDetails.getUsername())));
	}

	// Provider views their bookings
	@GetMapping("/provider-bookings")
	@PreAuthorize("hasRole('PROVIDER')")
	public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookingsAsProvider(
			@AuthenticationPrincipal UserDetails userDetails) {

		return ResponseEntity.ok(ApiResponse.success("Bookings fetched!",
				bookingService.getMyBookingsAsProvider(userDetails.getUsername())));
	}

	// Get single booking
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		return ResponseEntity.ok(
				ApiResponse.success("Booking fetched!", bookingService.getBookingById(id, userDetails.getUsername())));
	}
}