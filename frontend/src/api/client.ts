import axios from "axios";

/**
 * Shared Axios instance for all REST calls.
 *
 * - Requests go to `/api`, proxied to the backend by Vite (dev) and nginx (Docker).
 * - The stored JWT is attached as a Bearer token.
 * - A 401 on an authenticated request means the session expired: the token
 *   is cleared and the user is sent to the login page.
 */
const api = axios.create({
    baseURL: "/api",
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        // /auth/* returns 401 for wrong credentials; let the form show the
        // error instead of reloading the login page.
        const isAuthRequest = error.config?.url?.startsWith("/auth/");
        if (error.response?.status === 401 && !isAuthRequest) {
            localStorage.removeItem("token");
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

export default api;
