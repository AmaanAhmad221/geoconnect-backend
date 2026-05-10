package com.geoconnect.backend.repository;

import com.geoconnect.backend.entity.Booking;
import com.geoconnect.backend.entity.BookingStatus;
import com.geoconnect.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByCustomer(User customer);

	List<Booking> findByProvider(User provider);

	List<Booking> findByCustomerAndStatus(User customer, BookingStatus status);

	List<Booking> findByProviderAndStatus(User provider, BookingStatus status);

	long countByStatus(BookingStatus status); 
}