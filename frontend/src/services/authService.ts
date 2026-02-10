import api from './api';
import type { LoginRequest, LoginResponse } from '../types/api';

export const login = (data: LoginRequest) =>
  api.post<LoginResponse>('/auth/login', data).then((res) => res.data);
