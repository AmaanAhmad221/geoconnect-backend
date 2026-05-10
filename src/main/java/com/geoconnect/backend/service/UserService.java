package com.geoconnect.backend.service;

import com.geoconnect.backend.dto.AuthResponse;
import com.geoconnect.backend.dto.LoginRequest;
import com.geoconnect.backend.dto.RegisterRequest;
import com.geoconnect.backend.entity.User;

public interface UserService {
	User registerUser(RegisterRequest request);
	
	AuthResponse login(LoginRequest request);

}
