import { removeAuthToken, setAuthToken } from "../api/auth";

export function saveToken(token: string) {
  localStorage.setItem("jwt", token);
  setAuthToken(token);
}

export function clearToken() {
  localStorage.removeItem("jwt");
  removeAuthToken();
}

export function getStoredToken(): string | null {
  return localStorage.getItem("jwt");
}