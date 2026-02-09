import api from './api';
import type { CreateEventRequest, EventResponse, ParticipantResponse, RegistrationRequest } from '../types/api';

export const getEvents = () =>
  api.get<EventResponse[]>('/events').then((res) => res.data);

export const getEventById = (id: number) =>
  api.get<EventResponse>(`/events/${id}`).then((res) => res.data);

export const createEvent = (data: CreateEventRequest) =>
  api.post<EventResponse>('/events', data).then((res) => res.data);

export const getParticipants = (eventId: number) =>
  api.get<ParticipantResponse[]>(`/events/${eventId}/participants`).then((res) => res.data);

export const registerParticipant = (eventId: number, data: RegistrationRequest) =>
  api.post<ParticipantResponse>(`/events/${eventId}/participants`, data).then((res) => res.data);