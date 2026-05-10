package com.geoconnect.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.geoconnect.backend.dto.ChatMessageRequest;
import com.geoconnect.backend.dto.ChatMessageResponse;
import com.geoconnect.backend.entity.Booking;
import com.geoconnect.backend.entity.ChatMessage;
import com.geoconnect.backend.entity.User;
import com.geoconnect.backend.repository.BookingRepository;
import com.geoconnect.backend.repository.ChatMessageRepository;
import com.geoconnect.backend.repository.UserRepository;
import com.geoconnect.backend.service.ChatService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final ChatMessageRepository chatMessageRepository;
	private final BookingRepository bookingRepository;
	private final UserRepository userRepository;

	@Override
	public ChatMessageResponse sendMessage(ChatMessageRequest request, String senderUsername) {

		User sender = userRepository.findByUsername(senderUsername)
				.orElseThrow(() -> new RuntimeException("Sender not found!"));

		User receiver = userRepository.findByUsername(request.getReceiverUsername())
				.orElseThrow(() -> new RuntimeException("Receiver not found!"));

		Booking booking = bookingRepository.findById(request.getBookingId())
				.orElseThrow(() -> new RuntimeException("Booking not found!"));

		// ✅ FIXED — get username from customer and provider directly
		String customerUsername = booking.getCustomer().getUsername();
		String providerUsername = booking.getProvider().getUsername();

		System.out.println("=== BOOKING CHECK ===");
		System.out.println("Customer: " + customerUsername);
		System.out.println("Provider: " + providerUsername);
		System.out.println("Sender: " + senderUsername);

		boolean isSenderCustomer = customerUsername.equals(senderUsername);
		boolean isSenderProvider = providerUsername.equals(senderUsername);

		if (!isSenderCustomer && !isSenderProvider) {
			throw new RuntimeException("You are not part of this booking!");
		}

		ChatMessage message = new ChatMessage();
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setBooking(booking);
		message.setContent(request.getContent());
		message.setRead(false);

		return ChatMessageResponse.fromMessage(chatMessageRepository.save(message));
	}

	@Override
	public List<ChatMessageResponse> getChatHistory(Long bookingId, String username) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found!"));

		boolean isCustomer = booking.getCustomer().getUsername().equals(username);
		boolean isProvider = booking.getProvider().getUsername().equals(username);

		if (!isCustomer && !isProvider) {
			throw new RuntimeException("You don't have access to this chat!");
		}
		return chatMessageRepository.findByBookingOrderBySentAtAsc(booking).stream()
				.map(ChatMessageResponse::fromMessage).collect(Collectors.toList());
	}

	@Override
	public List<ChatMessageResponse> getUnreadMessages(String username) {
		User receiver = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found!"));

		return chatMessageRepository.findByReceiverAndIsReadFalse(receiver).stream()
				.map(ChatMessageResponse::fromMessage).collect(Collectors.toList());
	}

	@Override
	public long getUnreadCount(String username) {
		User receiver = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found!"));
		return chatMessageRepository.countByReceiverAndIsReadFalse(receiver);
	}

	@Override
	public void markAsRead(Long bookingId, String username) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found!"));

		User receiver = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found!"));

		List<ChatMessage> unreadMessages = chatMessageRepository.findByBookingOrderBySentAtAsc(booking).stream()
				.filter(msg -> msg.getReceiver().getUsername().equals(username) && !msg.isRead())
				.collect(Collectors.toList());

		unreadMessages.forEach(msg -> msg.setRead(true));
		chatMessageRepository.saveAll(unreadMessages);

	}
}
