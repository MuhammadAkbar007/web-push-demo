const BASE_URL = "http://localhost:8080/api/v1/push";
export const SUBSCRIBE_URL = BASE_URL + "/subscribe";
export const VAPID_KEY_URL = BASE_URL + "/vapid-key";

// Convert Base64 to Uint8Array
export function urlBase64ToUint8Array(base64String) {
    const padding = "=".repeat((4 - (base64String.length % 4)) % 4);
    const base64 = (base64String + padding)
        .replace(/-/g, "+")
        .replace(/_/g, "/");

    const rawData = atob(base64);
    const outputArray = new Uint8Array(rawData.length);

    for (let i = 0; i < rawData.length; ++i) {
        outputArray[i] = rawData.charCodeAt(i);
    }

    return outputArray;
}
