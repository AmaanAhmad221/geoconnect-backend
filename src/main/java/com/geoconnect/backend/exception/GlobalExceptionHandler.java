package com.geoconnect.backend.exception;

import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 404 - Resource not found
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
	}

	// 409 - Duplicate resource
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ApiResponse<?>> handleDuplicateResourceException(DuplicateResourceException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
	}

	// 403 - Unauthorized action
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiResponse<?>> handleUnauthorizedException(UnauthorizedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(ex.getMessage()));
	}

	// 400 - Bad request
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponse<?>> handleBadRequestException(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
	}

	// 400 - General runtime errors
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
	}

	// 400 - Validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<List<ValidationError>>> handleValidationException(
			MethodArgumentNotValidException ex) {

		List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponse<>(false, "Validation failed", errors));
	}

	// 403 - Spring Security access denied
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ApiResponse.error("Access denied! You don't have permission."));
	}

	// 401 - Not authenticated
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ApiResponse.error("Unauthorized! Please login first."));
	}
}