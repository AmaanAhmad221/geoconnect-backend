package com.geoconnect.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.geoconnect.backend.security.JwtAuthEntryPoint;
import com.geoconnect.backend.security.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final JwtAuthEntryPoint jwtAuthEntryPoint;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configure(http)).csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))
				.authorizeHttpRequests(auth -> auth

						// ✅ Auth & public infra
						.requestMatchers("/api/auth/**").permitAll().requestMatchers("/api/health").permitAll()
						.requestMatchers("/ws/**").permitAll().requestMatchers("/swagger-ui/**").permitAll()
						.requestMatchers("/api-docs/**").permitAll().requestMatchers("/v3/api-docs/**").permitAll()
						.requestMatchers("/uploads/**").permitAll().requestMatchers("/favicon.ico").permitAll()

						// ✅ Public read-only APIs (no login needed)
						.requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/location/nearby-services").permitAll()

						// 🔐 EVERYTHING ELSE NEEDS LOGIN
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}