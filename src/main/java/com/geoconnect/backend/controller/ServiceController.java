package com.geoconnect.backend.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.PageResponse;
import com.geoconnect.backend.dto.ServiceRequest;
import com.geoconnect.backend.dto.ServiceResponse;
import com.geoconnect.backend.entity.ServiceCategory;
import com.geoconnect.backend.service.GeoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "Service marketplace APIs")
public class ServiceController {

    private final GeoService geoService;

    @PostMapping
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(
            @Valid @RequestBody ServiceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success("Service created successfully!",
                geoService.createService(request, userDetails.getUsername()))
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success("Service updated successfully!",
                geoService.updateService(id, request, userDetails.getUsername()))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<String>> deleteService(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        geoService.deleteService(id, userDetails.getUsername());
        return ResponseEntity.ok(
            ApiResponse.success("Service deleted successfully!", null)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("Service fetched!",
                geoService.getServiceById(id))
        );
    }

    //  Paginated — GET /api/services?page=0&size=10&sort=price,asc
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ServiceResponse>>>
            getAllServices(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "createdAt") String sortBy,
                @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
            ApiResponse.success("Services fetched!",
                geoService.getAllServices(pageable))
        );
    }

    //  Search — GET /api/services/search?keyword=AC&page=0&size=10
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ServiceResponse>>>
            searchServices(
                @RequestParam String keyword,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
            ApiResponse.success("Search results!",
                geoService.searchServices(keyword, pageable))
        );
    }

    //  Filter — GET /api/services/filter?city=Indore&minPrice=100&maxPrice=500
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<PageResponse<ServiceResponse>>>
            filterServices(
                @RequestParam(required = false) String city,
                @RequestParam(required = false) ServiceCategory category,
                @RequestParam(required = false) Double minPrice,
                @RequestParam(required = false) Double maxPrice,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "price") String sortBy,
                @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
            ApiResponse.success("Filtered services!",
                geoService.searchWithFilters(
                    city, category, minPrice, maxPrice, pageable))
        );
    }

    //  By category — paginated
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<PageResponse<ServiceResponse>>>
            getByCategory(
                @PathVariable ServiceCategory category,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
            ApiResponse.success("Services fetched!",
                geoService.getServicesByCategory(category, pageable))
        );
    }

    //  By city — paginated
    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<PageResponse<ServiceResponse>>>
            getByCity(
                @PathVariable String city,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
            ApiResponse.success("Services fetched!",
                geoService.getServicesByCity(city, pageable))
        );
    }

    @GetMapping("/my-services")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getMyServices(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success("Your services fetched!",
                geoService.getMyServices(userDetails.getUsername()))
        );
    }
}