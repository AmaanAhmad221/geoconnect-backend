package com.geoconnect.backend.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {
	private Long bookingId;
	private String receiverUsername;
	private String content;
}
