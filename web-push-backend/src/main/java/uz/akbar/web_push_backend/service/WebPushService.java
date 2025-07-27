package uz.akbar.web_push_backend.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import uz.akbar.web_push_backend.payload.request.SubscriptionDto;
import uz.akbar.web_push_backend.payload.response.AppResponse;

public interface WebPushService {

	AppResponse<String> sendPublicKey();

	boolean addSubscription(SubscriptionDto dto);

	ResponseEntity<?> getCachedVapidKeyResponse(HttpServletRequest request);

	void sendNotificationToAll(String message);
}
