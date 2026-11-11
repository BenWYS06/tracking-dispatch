import { Client, type StompConfig } from "@stomp/stompjs";
import SockJS from "sockjs-client";

/**
 * SockJS endpoint exposed by the backend (`WebSocketConfig`).
 *
 * Relative on purpose: Vite dev server and nginx both proxy `/ws` to the
 * backend, so the same build works locally and in Docker.
 */
export const WS_ENDPOINT = "/ws";

/** Create a STOMP client over SockJS. Call `activate()` to connect. */
export function createStompClient(config: StompConfig = {}): Client {
  return new Client({
    webSocketFactory: () => new SockJS(WS_ENDPOINT),
    reconnectDelay: 5000,
    ...config,
  });
}
