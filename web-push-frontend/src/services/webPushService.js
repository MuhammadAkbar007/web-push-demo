import {
    SUBSCRIBE_URL,
    VAPID_KEY_URL,
    urlBase64ToUint8Array,
} from "../utils/webPushUtils";

export const subscribeUser = async () => {
    const permission = await Notification.requestPermission();
    if (permission !== "granted") {
        console.log("Notification permission not granted");
        return;
    }

    const registration = await navigator.serviceWorker.ready;
    let subscription = await registration.pushManager.getSubscription();

    if (!subscription) {
        const response = await fetch(VAPID_KEY_URL);
        const result = await response.json();

        if (!result.success) {
            console.error("Failed to retrieve VAPID key:", result.message);
            return;
        }

        // NOTE: browser caches this object because of Cache-Control header
        // and browser handles eTag as well
        const vapidKey = result.data;

        try {
            subscription = await registration.pushManager.subscribe({
                userVisibleOnly: true,
                applicationServerKey: urlBase64ToUint8Array(vapidKey),
            });
        } catch (err) {
            console.error("Push subscription failed:", err.name, err.message);
        }
    }

    await fetch(SUBSCRIBE_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(subscription),
    });
};
