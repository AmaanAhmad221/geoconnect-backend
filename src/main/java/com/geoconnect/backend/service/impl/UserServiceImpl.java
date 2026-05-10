package com.geoconnect.backend.service.impl;

import com.geoconnect.backend.dto.AuthResponse;
import com.geoconnect.backend.dto.LoginRequest;
import com.geoconnect.backend.dto.RegisterRequest;
import com.geoconnect.backend.dto.UserResponse;
import com.geoconnect.backend.entity.Role;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.exception.BadRequestException;
import com.geoconnect.backend.exception.DuplicateResourceException;
import com.geoconnect.backend.exception.ResourceNotFoundException;
import com.geoconnect.backend.repository.UserRepository;
import com.geoconnect.backend.security.JwtUtils;
import com.geoconnect.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;

	@Override
	public User registerUser(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new DuplicateResourceException("Email already registered!");
		}

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new DuplicateResourceException("Username already taken!");
		}

		User user = new User();
		user.setName(request.getName());
		user.setUsername(request.getUsername().toLowerCase());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhone(request.getPhone());
		user.setRole(Role.CUSTOMER);

		return userRepository.save(user);
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with username/email: " + request.getUsernameOrEmail()));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadRequestException("Invalid password!");
		}

		String token = jwtUtils.generateToken(user.getUsername());
		return new AuthResponse(token, UserResponse.fromUser(user));
	}
}