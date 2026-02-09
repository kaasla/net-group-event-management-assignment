import { createContext } from 'react';
import type { LoginRequest } from '../types/api';

export interface AuthContextValue {
  token: string | null;
  isAdmin: boolean;
  login: (data: LoginRequest) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextValue | null>(null);