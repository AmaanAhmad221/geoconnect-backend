package com.geoconnect.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geoconnect.backend.entity.Booking;
import com.geoconnect.backend.entity.ChatMessage;
import com.geoconnect.backend.entity.User;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{
	
	List<ChatMessage> findByBookingOrderBySentAtAsc(Booking booking);
	
	List<ChatMessage> findByReceiverAndIsReadFalse(User receiver);
	
	long countByReceiverAndIsReadFalse(User receiver);
	
}
