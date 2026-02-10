export interface EventResponse {
  id: number;
  name: string;
  dateTime: string;
  maxParticipants: number;
  participantCount: number;
  availableSpots: number;
  createdAt: string;
}

export interface CreateEventRequest {
  name: string;
  dateTime: string;
  maxParticipants: number;
}

export interface ParticipantResponse {
  id: number;
  firstName: string;
  lastName: string;
  personalCode: string;
  createdAt: string;
}

export interface RegistrationRequest {
  firstName: string;
  lastName: string;
  personalCode: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  expiresIn: number;
}

export interface FieldError {
  field: string;
  message: string;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  fieldErrors: FieldError[] | null;
}
