self.addEventListener("push", function (event) {
    console.log("[Service Worker] Push Received.");

    // NOTE: backend sends json including vars: title, body
    const data = event.data?.json() || {
        title: "You have a new message! 📩",
        body: "Notification ✍️",
    };

    const options = {
        body: data.body,
        icon: "/icon.png", // NOTE: 192x192 recommended inside public/ folder
        badge: "/badge.png", // NOTE: 72x72 or 96x96 in monochrome inside public/ folder
    };

    event.waitUntil(self.registration.showNotification(data.title, options));
});

self.addEventListener("notificationclick", function (event) {
    console.log("[Service Worker] Notification click received.");
    event.notification.close();

    event.waitUntil(
        self.clients.openWindow("/"), // NOTE: open home page, or set it to a specific URL
        // self.clients.openWindow(event.notification.data.clickActionUrl) // NOTE: backend can send specific url
    );
});
