package com.geoconnect.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private User customer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id")
	private User provider;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_id")
	private Service bookedService;

	@Enumerated(EnumType.STRING)
	private BookingStatus status = BookingStatus.PENDING;

	private String notes;

	private String address;

	private Double latitude;

	private Double longitude;

	@Column(name = "scheduled_at")
	private LocalDateTime scheduledAt;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();
}