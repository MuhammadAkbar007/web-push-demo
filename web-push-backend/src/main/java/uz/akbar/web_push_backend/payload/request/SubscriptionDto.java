package uz.akbar.web_push_backend.payload.request;

import java.util.Map;

public record SubscriptionDto(
		String endpoint,
		Map<String, String> keys) {
}
