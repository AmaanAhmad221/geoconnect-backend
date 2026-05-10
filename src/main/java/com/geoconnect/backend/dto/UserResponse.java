package com.geoconnect.backend.dto;

import com.geoconnect.backend.entity.Role;
import com.geoconnect.backend.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

	private Long id;
	private String name;
	private String username;
	private String email;
	private String phone;
	private Role role;
	private String profilePhoto;
	private LocalDateTime createdAt;

	public static UserResponse fromUser(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setName(user.getName());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setPhone(user.getPhone());
		response.setRole(user.getRole());
		response.setProfilePhoto(user.getProfilePhoto());
		response.setCreatedAt(user.getCreatedAt());
		return response;
	}
}