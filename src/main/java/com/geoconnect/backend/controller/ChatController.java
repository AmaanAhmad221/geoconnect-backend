package com.geoconnect.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoconnect.backend.dto.ApiResponse;
import com.geoconnect.backend.dto.ChatMessageRequest;
import com.geoconnect.backend.dto.ChatMessageResponse;
import com.geoconnect.backend.service.ChatService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Real-time chat APIs")
public class ChatController {

	private final ChatService chatService;
	private final SimpMessagingTemplate messagingTemplate;

	// WebSocket endpoint
	@MessageMapping("/chat.send")
	public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
		ChatMessageResponse response = chatService.sendMessage(request, principal.getName());
		messagingTemplate.convertAndSendToUser(request.getReceiverUsername(), "/queue/messages", response);
		messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/messages", response);
	}

	// Send message via HTTP
	@PostMapping("/send")
	public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessageHttp(@RequestBody ChatMessageRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {

		ChatMessageResponse response = chatService.sendMessage(request, userDetails.getUsername());
		messagingTemplate.convertAndSendToUser(request.getReceiverUsername(), "/queue/messages", response);
		return ResponseEntity.ok(ApiResponse.success("Message sent!", response));
	}

	// Get chat history
	@GetMapping("/history/{bookingId}")
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatHistory(@PathVariable Long bookingId,
			@AuthenticationPrincipal UserDetails userDetails) {

		List<ChatMessageResponse> history = chatService.getChatHistory(bookingId, userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Chat history fetched!", history));
	}

	// Get unread messages
	@GetMapping("/unread")
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getUnreadMessages(
			@AuthenticationPrincipal UserDetails userDetails) {

		List<ChatMessageResponse> unread = chatService.getUnreadMessages(userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Unread messages fetched!", unread));
	}

	// Get unread count ← THIS WAS MISSING!
	@GetMapping("/unread/count")
	public ResponseEntity<ApiResponse<Long>> getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {

		long count = chatService.getUnreadCount(userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Unread count fetched!", count));
	}

	// Mark messages as read
	@PutMapping("/read/{bookingId}")
	public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long bookingId,
			@AuthenticationPrincipal UserDetails userDetails) {

		chatService.markAsRead(bookingId, userDetails.getUsername());
		return ResponseEntity.ok(ApiResponse.success("Messages marked as read!", null));
	}
}