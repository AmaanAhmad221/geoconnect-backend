package com.geoconnect.backend.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.geoconnect.backend.dto.PageResponse;
import com.geoconnect.backend.dto.ServiceRequest;
import com.geoconnect.backend.dto.ServiceResponse;
import com.geoconnect.backend.entity.ServiceCategory;

public interface GeoService {

	ServiceResponse createService(ServiceRequest request, String username);

	ServiceResponse updateService(Long id, ServiceRequest request, String username);

	void deleteService(Long id, String username);

	ServiceResponse getServiceById(Long id);

	// ✅ Paginated versions
	PageResponse<ServiceResponse> getAllServices(Pageable pageable);

	PageResponse<ServiceResponse> getServicesByCategory(ServiceCategory category, Pageable pageable);

	PageResponse<ServiceResponse> getServicesByCity(String city, Pageable pageable);

	// ✅ Search & Filter
	PageResponse<ServiceResponse> searchServices(String keyword, Pageable pageable);

	PageResponse<ServiceResponse> searchWithFilters(String city, ServiceCategory category, Double minPrice,
			Double maxPrice, Pageable pageable);

	// Keep old ones for location module
	List<ServiceResponse> getAllServices();

	List<ServiceResponse> getMyServices(String username);
}