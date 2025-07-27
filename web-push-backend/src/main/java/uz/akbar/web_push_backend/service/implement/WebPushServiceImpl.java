package uz.akbar.web_push_backend.service.implement;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import uz.akbar.web_push_backend.payload.request.SubscriptionDto;
import uz.akbar.web_push_backend.payload.response.AppResponse;
import uz.akbar.web_push_backend.service.WebPushService;

@Service
public class WebPushServiceImpl implements WebPushService {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Value("${vapid.public.key}")
	private String publicKey;

	@Value("${vapid.private.key}")
	private String privateKey;

	private Set<SubscriptionDto> subscriptions = new HashSet<>();

	@Override
	public AppResponse<String> sendPublicKey() {
		return AppResponse.success("Public key for WebPush", publicKey);
	}

	@Override
	public boolean addSubscription(SubscriptionDto dto) {
		if (subscriptions.contains(dto))
			return false;

		subscriptions.add(dto);
		System.out.println();
		System.out.println(subscriptions);

		return true;
	}

	@Override
	public ResponseEntity<?> getCachedVapidKeyResponse(HttpServletRequest request) {
		String etag = generateEtag(publicKey);
		String ifNoneMatch = request.getHeader("If-None-Match");

		// NOTE: cache for 1 day & don't fetch in this period of time
		CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic();

		// No change
		if (etag.equals(ifNoneMatch))
			return ResponseEntity
					.status(HttpStatus.NOT_MODIFIED)
					.cacheControl(cacheControl)
					.eTag(etag)
					.build();

		return ResponseEntity.ok()
				.cacheControl(cacheControl)
				.eTag(etag)
				.body(AppResponse.success("public vapid key", publicKey));

	}

	@Override
	public void sendNotificationToAll(String message) {
		for (SubscriptionDto sub : subscriptions) {
			try {
				sendPushMessage(sub, message);
			} catch (Exception e) {
				System.err.println("Failed to send to: " + sub.endpoint());
				e.printStackTrace();
			}
		}
	}

	private void sendPushMessage(SubscriptionDto subscriptionDto, String message) throws Exception {
		PushService pushService = new PushService();
		pushService.setPublicKey(publicKey);
		pushService.setPrivateKey(privateKey);

		String p256dh = subscriptionDto.keys().get("p256dh");
		String auth = subscriptionDto.keys().get("auth");

		Subscription subscription = new Subscription(
				subscriptionDto.endpoint(),
				new Subscription.Keys(p256dh, auth));

		Map<String, String> payloadMap = Map.of(
				"title", "New Notification 🔔",
				"body", message);

		String payloadJson = objectMapper.writeValueAsString(payloadMap);

		Notification notification = new Notification(subscription, payloadJson);
		pushService.send(notification);
	}

	private String generateEtag(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

			return "\"" + Base64.getEncoder().encodeToString(hash) + "\"";
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate ETag", e);
		}
	}

}
