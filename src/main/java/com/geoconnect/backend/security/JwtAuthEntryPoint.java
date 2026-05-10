package com.geoconnect.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geoconnect.backend.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ApiResponse<?> apiResponse = ApiResponse.error("Unauthorized! Please login first.");

		new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
	}
}