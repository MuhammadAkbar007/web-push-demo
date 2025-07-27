import { useEffect } from "react";
import { subscribeUser } from "./services/webPushService";
import "./App.css";

function App() {
    // ask notification permission after the first touch
    useEffect(() => {
        const handler = () => {
            if (Notification.permission === "default") {
                subscribeUser();
            }
            document.removeEventListener("click", handler);
        };

        document.addEventListener("click", handler);

        return () => {
            document.removeEventListener("click", handler);
        };
    }, []);

    return (
        <div>
            <h1>Push Notification Demo</h1>
            <p>You’ll get a notification when it is sent!</p>
            <button onClick={subscribeUser}>Enable Notifications</button>
        </div>
    );
}

export default App;
