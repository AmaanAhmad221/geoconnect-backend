package com.geoconnect.backend.service;

import java.util.List;

import com.geoconnect.backend.dto.BookingRequest;
import com.geoconnect.backend.dto.BookingResponse;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request, String username);

    BookingResponse acceptBooking(Long bookingId, String username);

    BookingResponse rejectBooking(Long bookingId, String username);

    BookingResponse cancelBooking(Long bookingId, String username);

    BookingResponse completeBooking(Long bookingId, String username);

    List<BookingResponse> getMyBookingsAsCustomer(String username);

    List<BookingResponse> getMyBookingsAsProvider(String username);

    BookingResponse getBookingById(Long bookingId, String username);
}