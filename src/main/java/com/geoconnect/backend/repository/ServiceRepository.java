package com.geoconnect.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.geoconnect.backend.entity.Service;
import com.geoconnect.backend.entity.ServiceCategory;
import com.geoconnect.backend.entity.User;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

	// ── List queries (no pagination) ──────────────────────────
	List<Service> findByProvider(User provider);

	List<Service> findByAvailableTrue();

	List<Service> findByCategoryAndAvailableTrue(ServiceCategory category);

	List<Service> findByCityAndAvailableTrue(String city);

	List<Service> findByCityAndCategoryAndAvailableTrue(String city, ServiceCategory category);

	// ── Basic paginated ───────────────────────────────────────
	Page<Service> findByAvailableTrue(Pageable pageable);

	Page<Service> findByCategoryAndAvailableTrue(ServiceCategory category, Pageable pageable);

	Page<Service> findByCityAndAvailableTrue(String city, Pageable pageable);

	// ── Price range ───────────────────────────────────────────
	Page<Service> findByAvailableTrueAndPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

	// ── Category + price range ────────────────────────────────
	Page<Service> findByCategoryAndAvailableTrueAndPriceBetween(ServiceCategory category, Double minPrice,
			Double maxPrice, Pageable pageable);

	// ── City + price range ────────────────────────────────────
	Page<Service> findByCityIgnoreCaseAndAvailableTrueAndPriceBetween(String city, Double minPrice, Double maxPrice,
			Pageable pageable);

	// ── City + category + price range ─────────────────────────
	Page<Service> findByAvailableTrueAndCityIgnoreCaseAndCategoryAndPriceBetween(String city, ServiceCategory category,
			Double minPrice, Double maxPrice, Pageable pageable);

	// ── City + category (no price) ────────────────────────────
	Page<Service> findByCityIgnoreCaseAndCategoryAndAvailableTrue(String city, ServiceCategory category,
			Pageable pageable);

	// ── Keyword search ────────────────────────────────────────
	@Query("SELECT s FROM Service s WHERE s.available = true AND "
			+ "(LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	Page<Service> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}