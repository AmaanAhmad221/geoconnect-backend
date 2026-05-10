package com.geoconnect.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

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
	@JoinColumn(name = "booking_id")
	private Booking booking;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_id")
	private Service service;

	@Min(1)
	@Max(5)
	private int rating;

	@Column(length = 1000)
	private String comment;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
}
