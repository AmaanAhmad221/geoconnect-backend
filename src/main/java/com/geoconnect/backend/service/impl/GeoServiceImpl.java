package com.geoconnect.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.geoconnect.backend.dto.PageResponse;
import com.geoconnect.backend.dto.ServiceRequest;
import com.geoconnect.backend.dto.ServiceResponse;
import com.geoconnect.backend.entity.Role;
import com.geoconnect.backend.entity.Service;
import com.geoconnect.backend.entity.ServiceCategory;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.exception.BadRequestException;
import com.geoconnect.backend.exception.ResourceNotFoundException;
import com.geoconnect.backend.exception.UnauthorizedException;
import com.geoconnect.backend.repository.ServiceRepository;
import com.geoconnect.backend.repository.UserRepository;
import com.geoconnect.backend.service.GeoService;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class GeoServiceImpl implements GeoService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @Override
    public ServiceResponse createService(ServiceRequest request, String username) {
        User provider = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (provider.getRole() == Role.CUSTOMER) {
            throw new BadRequestException("Only providers can create services!");
        }

        Service service = new Service();
        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setCategory(request.getCategory());
        service.setCity(request.getCity().toLowerCase());
        service.setArea(request.getArea().toLowerCase());
        service.setLatitude(request.getLatitude());
        service.setLongitude(request.getLongitude());
        service.setProvider(provider);
        service.setAvailable(true);

        return ServiceResponse.fromService(serviceRepository.save(service));
    }

    @Override
    public ServiceResponse updateService(Long id, ServiceRequest request, String username) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        if (!service.getProvider().getUsername().equals(username)) {
            throw new UnauthorizedException("You can only update your own services!");
        }

        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setCategory(request.getCategory());
        service.setCity(request.getCity().toLowerCase());
        service.setArea(request.getArea().toLowerCase());
        service.setLatitude(request.getLatitude());
        service.setLongitude(request.getLongitude());
        service.setUpdatedAt(LocalDateTime.now());

        return ServiceResponse.fromService(serviceRepository.save(service));
    }

    @Override
    public void deleteService(Long id, String username) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        if (!service.getProvider().getUsername().equals(username)) {
            throw new UnauthorizedException("You can only delete your own services!");
        }

        serviceRepository.delete(service);
    }

    @Override
    public ServiceResponse getServiceById(Long id) {
        return ServiceResponse.fromService(
                serviceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id)));
    }

    @Override
    public PageResponse<ServiceResponse> getAllServices(Pageable pageable) {
        Page<ServiceResponse> page = serviceRepository
                .findByAvailableTrue(pageable)
                .map(ServiceResponse::fromService);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<ServiceResponse> getServicesByCategory(
            ServiceCategory category, Pageable pageable) {
        Page<ServiceResponse> page = serviceRepository
                .findByCategoryAndAvailableTrue(category, pageable)
                .map(ServiceResponse::fromService);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<ServiceResponse> getServicesByCity(
            String city, Pageable pageable) {
        Page<ServiceResponse> page = serviceRepository
                .findByCityAndAvailableTrue(city.toLowerCase(), pageable)
                .map(ServiceResponse::fromService);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<ServiceResponse> searchServices(
            String keyword, Pageable pageable) {
        Page<ServiceResponse> page = serviceRepository
                .searchByKeyword(keyword, pageable)
                .map(ServiceResponse::fromService);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<ServiceResponse> searchWithFilters(
            String city, ServiceCategory category,
            Double minPrice, Double maxPrice, Pageable pageable) {

        Page<Service> page;

        boolean hasCity     = city != null && !city.isBlank();
        boolean hasCategory = category != null;
        boolean hasPrice    = minPrice != null && maxPrice != null;

        if (hasCity && hasCategory && hasPrice) {
            // All 3 filters
            page = serviceRepository
                    .findByAvailableTrueAndCityIgnoreCaseAndCategoryAndPriceBetween(
                            city, category, minPrice, maxPrice, pageable);

        } else if (hasCity && hasCategory) {
            // City + category only
            page = serviceRepository
                    .findByCityIgnoreCaseAndCategoryAndAvailableTrue(
                            city, category, pageable);

        } else if (hasCategory && hasPrice) {
            // Category + price only
            page = serviceRepository
                    .findByCategoryAndAvailableTrueAndPriceBetween(
                            category, minPrice, maxPrice, pageable);

        } else if (hasCity && hasPrice) {
            // City + price only
            page = serviceRepository
                    .findByCityIgnoreCaseAndAvailableTrueAndPriceBetween(
                            city, minPrice, maxPrice, pageable);

        } else if (hasCategory) {
            // Category only ← THIS is what was failing before
            page = serviceRepository
                    .findByCategoryAndAvailableTrue(category, pageable);

        } else if (hasCity) {
            // City only
            page = serviceRepository
                    .findByCityAndAvailableTrue(city.toLowerCase(), pageable);

        } else if (hasPrice) {
            // Price range only
            page = serviceRepository
                    .findByAvailableTrueAndPriceBetween(minPrice, maxPrice, pageable);

        } else {
            // No filters — return all
            page = serviceRepository.findByAvailableTrue(pageable);
        }

        return PageResponse.fromPage(page.map(ServiceResponse::fromService));
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findByAvailableTrue().stream()
                .map(ServiceResponse::fromService)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponse> getMyServices(String username) {
        User provider = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", "username", username));
        return serviceRepository.findByProvider(provider).stream()
                .map(ServiceResponse::fromService)
                .collect(Collectors.toList());
    }
}