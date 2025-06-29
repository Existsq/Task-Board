import { useState, useEffect } from "react";
import { getMeApi, loginApi, registerApi } from "../api/auth";
import { saveToken, clearToken, getStoredToken } from "../utils/token";
import { User } from "../context/AuthContext";

export function useAuthProvider() {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = getStoredToken();
    if (!token) {
      setIsLoading(false);
      return;
    }

    saveToken(token);

    getMeApi()
      .then((user) => {
        setUser(user);
        setIsAuthenticated(true);
      })
      .catch(() => {
        setUser(null);
        setIsAuthenticated(false);
      })
      .finally(() => setIsLoading(false));
  }, []);

  async function login(email: string, password: string) {
    const token = await loginApi(email, password);
    saveToken(token);
    const user = await getMeApi();
    setUser(user);
    setIsAuthenticated(true);
  }

  async function register(email: string, password: string) {
    const token = await registerApi(email, password);
    saveToken(token);
    const user = await getMeApi();
    setUser(user);
    setIsAuthenticated(true);
  }

  function logout() {
    clearToken();
    setUser(null);
    setIsAuthenticated(false);
  }

  return { user, isAuthenticated, isLoading, login, register, logout };
}
