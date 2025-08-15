package uz.akbar.web_push_backend.controller;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uz.akbar.web_push_backend.payload.request.SubscriptionDto;
import uz.akbar.web_push_backend.service.WebPushService;
import uz.akbar.web_push_backend.utils.Utils;

@RestController
@RequestMapping(Utils.BASE_URL + "/push")
@RequiredArgsConstructor
public class WebPushController {

    private final WebPushService service;

    @GetMapping("/vapid-key")
    public ResponseEntity<?> sendPublicKey(HttpServletRequest request) {
        return service.getCachedVapidKeyResponse(request);
    }

    // @GetMapping("/vapid-key")
    // public ResponseEntity<AppResponse<String>> sendPublicKey() {
    // AppResponse<String> response = service.sendPublicKey();
    //
    // return ResponseEntity
    // .ok()
    // .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic()) // cache
    // for 1 day & don't fetch
    // .body(response);
    // }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody SubscriptionDto dto) {
        boolean response = service.addSubscription(dto);
        return response ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @PostMapping("/broadcast")
    public ResponseEntity<?> broadcast(@RequestParam String message) {
        service.sendNotificationToAll(message);
        return ResponseEntity.ok("Notification sent to all");
    }
}
