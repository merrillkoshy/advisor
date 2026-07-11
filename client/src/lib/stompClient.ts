import { BACKEND_URL } from "@/constants";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const stompClient = new Client({
  webSocketFactory: () => new SockJS(`${BACKEND_URL}/ws`),
  reconnectDelay: 5000,
});

export default stompClient;
