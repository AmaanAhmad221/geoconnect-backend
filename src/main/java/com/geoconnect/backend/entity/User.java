package com.geoconnect.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	@Column(unique = true)
	private String username;

	@Email
	@NotBlank
	@Column(unique = true)
	private String email;

	@NotBlank
	private String password;

	private String phone;
	
	private Double latitude;
	private Double longitude;
	private String City;
	private String address;
	private String profilePhoto;

	@Enumerated(EnumType.STRING)
	private Role role = Role.CUSTOMER;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

}