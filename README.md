# Web Browsers support WebPush:
## full support
 - [x] Chrome
 - [x] Firefox
 - [x] Edge
 - [x] Opera

## not support
 - ❌ Brave
 - ❌ Safari (if not PWA)
 - ❌ Samsung Internet

# 1. create react app
## Frontend
```bash
npm create vite@latest my-app
```

# 2. create service worker js file inside public package folder 
## Frontend
```js
self.addEventListener("push", function (event) {
    console.log("[Service Worker] Push Received.");

    // NOTE: backend sends json including vars: title, body
    const data = event.data?.json() || {
        title: "You have a new message! 📩",
        body: "Notification ✍️",
    };

    const options = {
        body: data.body,
        icon: "/icon.png", // TODO: 192x192 recommended inside public/ folder
        badge: "/badge.png", // TODO: 72x72 or 96x96 in monochrome inside public/ folder
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

```

# 3. register sw in main.jsx
## Frontend
```jsx
if ("serviceWorker" in navigator) {
    window.addEventListener("load", () => {
        navigator.serviceWorker
            .register("/service-worker.js")
            .then((registration) => {
                console.log(
                    "✅ Service Worker registered with scope:",
                    registration.scope,
                );
            })
            .catch((error) => {
                console.error("❌ Service Worker registration failed:", error);
            });
    });
}
```

# 4. generate a backend project
## Backend
[spring generator site](https://start.spring.io) with the following dependencies:
*Spring Web*, *Lombok*

# 5. create api to send vapi public key
## Backend
Get public key from `application.properties` and return as api response data

# 6. create api to save subscription object
## Backend
Create **@PostMapping** with **DTO**

# 7. subscribe user (browser)
## Frontend
 - get permission to send notification
    - this gives subscription object
 - prepare vapid public key
    - get from cache or fetch from backend
 - set subscription object with valid fields
 - send subscription object to backend

# 8. send notification from saved subscription object
## Backend
Create **@PostMapping** to send notification

