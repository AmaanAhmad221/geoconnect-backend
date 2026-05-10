package com.geoconnect.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	@Column(length = 1000)
	private String description;

	@NotNull
	@Positive
	private Double price;

	@Enumerated(EnumType.STRING)
	private ServiceCategory category;

	private String city;

	private String area;
	
	private Double latitude;
	
	private Double longitude;

	private boolean available = true;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id")
	private User provider;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();
}