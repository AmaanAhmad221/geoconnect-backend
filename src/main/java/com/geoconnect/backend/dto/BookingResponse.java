package com.geoconnect.backend.dto;

import com.geoconnect.backend.entity.Booking;
import com.geoconnect.backend.entity.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponse {

	private Long id;
	private Long serviceId;
	private String serviceTitle;
	private Double servicePrice;
	private Long customerId;
	private String customerName;
	private String customerUsername;
	private Long providerId;
	private String providerName;
	private String providerUsername;
	private BookingStatus status;
	private String notes;
	private String address;
	private Double latitude;
	private Double longitude;
	private LocalDateTime scheduledAt;
	private LocalDateTime createdAt;

	public static BookingResponse fromBooking(Booking booking) {
		BookingResponse response = new BookingResponse();
		response.setId(booking.getId());

		// Use bookedService instead of service
		response.setServiceId(booking.getBookedService().getId());
		response.setServiceTitle(booking.getBookedService().getTitle());
		response.setServicePrice(booking.getBookedService().getPrice());

		response.setCustomerId(booking.getCustomer().getId());
		response.setCustomerName(booking.getCustomer().getName());
		response.setCustomerUsername(booking.getCustomer().getUsername());

		response.setProviderId(booking.getProvider().getId());
		response.setProviderName(booking.getProvider().getName());
		response.setProviderUsername(booking.getProvider().getUsername());

		response.setStatus(booking.getStatus());
		response.setNotes(booking.getNotes());
		response.setAddress(booking.getAddress());
		response.setLatitude(booking.getLatitude());
		response.setLongitude(booking.getLongitude());
		response.setScheduledAt(booking.getScheduledAt());
		response.setCreatedAt(booking.getCreatedAt());
		return response;
	}
}