import axios from "axios";

axios.defaults.baseURL = "/api";

export async function loginApi(email: string, password: string) {
  const res = await axios.post("/v1/auth/login", { email, password });
  return res.data.token;
}

export async function registerApi(email: string, password: string) {
  const res = await axios.post("/v1/auth/register", { email, password });
  return res.data.token;
}

export async function getMeApi() {
  const res = await axios.get("/v1/auth/me");
  return res.data;
}

export function setAuthToken(token: string) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

export function removeAuthToken() {
  delete axios.defaults.headers.common["Authorization"];
}