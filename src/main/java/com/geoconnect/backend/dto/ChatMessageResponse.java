package com.geoconnect.backend.dto;

import java.time.LocalDateTime;

import com.geoconnect.backend.entity.ChatMessage;

import lombok.Data;

@Data
public class ChatMessageResponse {

	private Long id;
	private Long bookingId;
	private String senderUsername;
	private String senderName;
	private String receiverUsername;
	private String content;
	private boolean read;
	private LocalDateTime sentAt;

	public static ChatMessageResponse fromMessage(ChatMessage message) {
		ChatMessageResponse response = new ChatMessageResponse();
		response.setId(message.getId());
		response.setBookingId(message.getBooking().getId());
		response.setSenderUsername(message.getSender().getUsername());
		response.setSenderName(message.getSender().getName());
		response.setReceiverUsername(message.getReceiver().getUsername());
		response.setContent(message.getContent());
		response.setRead(message.isRead());
		response.setSentAt(message.getSentAt());
		return response;
	}
}
