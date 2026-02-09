import { useState, useCallback, useMemo } from 'react';
import type { ReactNode } from 'react';
import { login as loginApi } from '../services/authService';
import { AuthContext } from './authContext';
import type { AuthContextValue } from './authContext';

export default function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(
    () => localStorage.getItem('token'),
  );

  const login = useCallback(async (data: Parameters<AuthContextValue['login']>[0]) => {
    const response = await loginApi(data);
    localStorage.setItem('token', response.token);
    setToken(response.token);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setToken(null);
  }, []);

  const value = useMemo<AuthContextValue>(
    () => ({ token, isAdmin: token !== null, login, logout }),
    [token, login, logout],
  );

  return <AuthContext value={value}>{children}</AuthContext>;
}