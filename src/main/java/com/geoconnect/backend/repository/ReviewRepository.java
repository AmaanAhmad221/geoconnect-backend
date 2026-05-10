package com.geoconnect.backend.repository;

import com.geoconnect.backend.entity.Booking;
import com.geoconnect.backend.entity.Review;
import com.geoconnect.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProvider(User provider);

    List<Review> findByCustomer(User customer);

    Optional<Review> findByBooking(Booking booking);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.provider = :provider")
    Double findAverageRatingByProvider(@Param("provider") User provider);

    long countByProvider(User provider);
}