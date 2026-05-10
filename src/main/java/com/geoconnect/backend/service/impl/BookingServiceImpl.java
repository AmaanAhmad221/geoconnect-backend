package com.geoconnect.backend.service.impl;

import com.geoconnect.backend.dto.BookingRequest;
import com.geoconnect.backend.dto.BookingResponse;
import com.geoconnect.backend.entity.Booking;
import com.geoconnect.backend.entity.BookingStatus;
import com.geoconnect.backend.entity.Service;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.exception.BadRequestException;
import com.geoconnect.backend.exception.ResourceNotFoundException;
import com.geoconnect.backend.exception.UnauthorizedException;
import com.geoconnect.backend.repository.BookingRepository;
import com.geoconnect.backend.repository.ServiceRepository;
import com.geoconnect.backend.repository.UserRepository;
import com.geoconnect.backend.service.BookingService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final ServiceRepository serviceRepository;
	private final UserRepository userRepository;

	@Override
	public BookingResponse createBooking(BookingRequest request, String username) {
		User customer = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		Service service = serviceRepository.findById(request.getServiceId())
				.orElseThrow(() -> new ResourceNotFoundException("Service", "id", request.getServiceId()));

		if (!service.isAvailable()) {
			throw new BadRequestException("Service is not available!");
		}

		if (service.getProvider().getUsername().equals(username)) {
			throw new BadRequestException("You cannot book your own service!");
		}

		Booking booking = new Booking();
		booking.setCustomer(customer);
		booking.setProvider(service.getProvider());
		booking.setBookedService(service);
		booking.setStatus(BookingStatus.PENDING);
		booking.setNotes(request.getNotes());
		booking.setAddress(request.getAddress());
		booking.setLatitude(request.getLatitude());
		booking.setLongitude(request.getLongitude());
		booking.setScheduledAt(request.getScheduledAt());

		return BookingResponse.fromBooking(bookingRepository.save(booking));
	}

	@Override
	public BookingResponse acceptBooking(Long bookingId, String username) {
		Booking booking = getBookingForProvider(bookingId, username);

		if (booking.getStatus() != BookingStatus.PENDING) {
			throw new BadRequestException("Only PENDING bookings can be accepted!");
		}

		booking.setStatus(BookingStatus.ACCEPTED);
		booking.setUpdatedAt(LocalDateTime.now());
		return BookingResponse.fromBooking(bookingRepository.save(booking));
	}

	@Override
	public BookingResponse rejectBooking(Long bookingId, String username) {
		Booking booking = getBookingForProvider(bookingId, username);

		if (booking.getStatus() != BookingStatus.PENDING) {
			throw new BadRequestException("Only PENDING bookings can be rejected!");
		}

		booking.setStatus(BookingStatus.REJECTED);
		booking.setUpdatedAt(LocalDateTime.now());
		return BookingResponse.fromBooking(bookingRepository.save(booking));
	}

	@Override
	public BookingResponse cancelBooking(Long bookingId, String username) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

		if (!booking.getCustomer().getUsername().equals(username)) {
			throw new UnauthorizedException("You can only cancel your own bookings!");
		}

		if (booking.getStatus() == BookingStatus.COMPLETED) {
			throw new BadRequestException("Completed bookings cannot be cancelled!");
		}

		booking.setStatus(BookingStatus.CANCELLED);
		booking.setUpdatedAt(LocalDateTime.now());
		return BookingResponse.fromBooking(bookingRepository.save(booking));
	}

	@Override
	public BookingResponse completeBooking(Long bookingId, String username) {
		Booking booking = getBookingForProvider(bookingId, username);

		if (booking.getStatus() != BookingStatus.ACCEPTED) {
			throw new BadRequestException("Only ACCEPTED bookings can be completed!");
		}

		booking.setStatus(BookingStatus.COMPLETED);
		booking.setUpdatedAt(LocalDateTime.now());
		return BookingResponse.fromBooking(bookingRepository.save(booking));
	}

	@Override
	public List<BookingResponse> getMyBookingsAsCustomer(String username) {
		User customer = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
		return bookingRepository.findByCustomer(customer).stream().map(BookingResponse::fromBooking)
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingResponse> getMyBookingsAsProvider(String username) {
		User provider = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
		return bookingRepository.findByProvider(provider).stream().map(BookingResponse::fromBooking)
				.collect(Collectors.toList());
	}

	@Override
	public BookingResponse getBookingById(Long bookingId, String username) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

		boolean isCustomer = booking.getCustomer().getUsername().equals(username);
		boolean isProvider = booking.getProvider().getUsername().equals(username);

		if (!isCustomer && !isProvider) {
			throw new UnauthorizedException("You don't have access to this booking!");
		}

		return BookingResponse.fromBooking(booking);
	}

	private Booking getBookingForProvider(Long bookingId, String username) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

		if (!booking.getProvider().getUsername().equals(username)) {
			throw new UnauthorizedException("You can only manage your own bookings!");
		}
		return booking;
	}
}