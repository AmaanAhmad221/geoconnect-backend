package com.geoconnect.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.UserResponse;
import com.geoconnect.backend.entity.Role;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.exception.BadRequestException;
import com.geoconnect.backend.exception.ResourceNotFoundException;
import com.geoconnect.backend.repository.UserRepository;
import com.geoconnect.backend.service.FileUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile APIs")
public class UserController {

	private final UserRepository userRepository;
	private final FileUploadService fileUploadService;

	@Value("${app.upload.dir}")
	private String uploadDir;

	// Get my profile
	@GetMapping("/me")
	@Operation(summary = "Get current user profile")
	public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {

		if (userDetails == null) {
			throw new RuntimeException("User not authenticated");
		}

		User user = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));

		return ResponseEntity.ok(ApiResponse.success("Profile fetched!", UserResponse.fromUser(user)));
	}

	// Upload profile photo
	@PostMapping("/profile-photo")
	@Operation(summary = "Upload profile photo")
	public ResponseEntity<ApiResponse<UserResponse>> uploadProfilePhoto(@RequestParam("file") MultipartFile file,
			@AuthenticationPrincipal UserDetails userDetails) {

		User user = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));

		// Delete old photo if exists
		if (user.getProfilePhoto() != null) {
			fileUploadService.deleteFile(user.getProfilePhoto());
		}

		// Upload new photo
		String fileName = fileUploadService.uploadProfilePhoto(file, user.getUsername());

		// Update user
		user.setProfilePhoto(fileName);
		userRepository.save(user);

		return ResponseEntity.ok(ApiResponse.success("Profile photo uploaded!", UserResponse.fromUser(user)));
	}

	// Delete profile photo
	@DeleteMapping("/profile-photo")
	@Operation(summary = "Delete profile photo")
	public ResponseEntity<ApiResponse<String>> deleteProfilePhoto(@AuthenticationPrincipal UserDetails userDetails) {

		User user = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));

		if (user.getProfilePhoto() != null) {
			fileUploadService.deleteFile(user.getProfilePhoto());
			user.setProfilePhoto(null);
			userRepository.save(user);
		}

		return ResponseEntity.ok(ApiResponse.success("Profile photo deleted!", null));
	}

	@PutMapping("/upgrade-to-provider")
	@Operation(summary = "Upgrade current user to PROVIDER role")
	public ResponseEntity<ApiResponse<UserResponse>> upgradeToProvider(
	        @AuthenticationPrincipal UserDetails userDetails) {

	    User user = userRepository.findByUsername(userDetails.getUsername())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                "User", "username", userDetails.getUsername()));

	    if (user.getRole() != Role.CUSTOMER) {
	        throw new BadRequestException("Only CUSTOMER accounts can upgrade to PROVIDER!");
	    }

	    user.setRole(Role.PROVIDER);
	    userRepository.save(user);

	    // Re-issue token with new role — user must re-login
	    return ResponseEntity.ok(ApiResponse.success(
	        "You are now a Provider! Please login again to activate your new role.",
	        UserResponse.fromUser(user)
	    ));
	
}
}