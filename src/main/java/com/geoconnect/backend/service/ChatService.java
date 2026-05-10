package com.geoconnect.backend.service;

import java.util.List;

import com.geoconnect.backend.dto.ChatMessageRequest;
import com.geoconnect.backend.dto.ChatMessageResponse;

public interface ChatService {

	ChatMessageResponse sendMessage(ChatMessageRequest request, String senderUsername);

    List<ChatMessageResponse> getChatHistory(Long bookingId, String username);

    List<ChatMessageResponse> getUnreadMessages(String username);

    long getUnreadCount(String username);

    void markAsRead(Long bookingId, String username);

}
