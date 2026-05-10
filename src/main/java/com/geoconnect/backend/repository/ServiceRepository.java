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

	List<Service> findByProvider(User provider);

	List<Service> findByAvailableTrue();

	List<Service> findByCategoryAndAvailableTrue(ServiceCategory category);

	List<Service> findByCityAndAvailableTrue(String city);

	List<Service> findByCityAndCategoryAndAvailableTrue(String city, ServiceCategory category);

	Page<Service> findByAvailableTrue(Pageable pageable);

	Page<Service> findByCategoryAndAvailableTrue(ServiceCategory category, Pageable pageable);

	Page<Service> findByCityAndAvailableTrue(String city, Pageable pageable);

	// Search by title or description
	@Query("SELECT s FROM Service s WHERE s.available = true AND "
			+ "(LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	Page<Service> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

	// Advanced search with filters
	@Query("SELECT s FROM Service s WHERE s.available = true AND "
			+ "(:city IS NULL OR LOWER(s.city) = LOWER(:city)) AND "
			+ "(:category IS NULL OR s.category = :category) AND " + "(:minPrice IS NULL OR s.price >= :minPrice) AND "
			+ "(:maxPrice IS NULL OR s.price <= :maxPrice)")
	Page<Service> searchWithFilters(@Param("city") String city, @Param("category") ServiceCategory category,
			@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);
}
