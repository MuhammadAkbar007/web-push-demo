package uz.akbar.web_push_backend.payload.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // excludes null and empty collections/arrays
public record AppResponse<T>(boolean success,
		String message,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp,
		T data) {

	// Static factory method with automatic timestamp
	public static <T> AppResponse<T> of(boolean success, String message, T data) {
		return new AppResponse<>(success, message, LocalDateTime.now(), data);
	}

	// Static factory method for success responses
	public static <T> AppResponse<T> success(String message, T data) {
		return new AppResponse<>(true, message, LocalDateTime.now(), data);
	}

	// Static factory method for error responses
	public static <T> AppResponse<T> error(String message) {
		return new AppResponse<>(false, message, LocalDateTime.now(), null);
	}
}
