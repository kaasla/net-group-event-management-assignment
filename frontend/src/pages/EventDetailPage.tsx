import { useEffect, useState, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import type { EventResponse, ParticipantResponse } from '../types/api';
import { getEventById, getParticipants } from '../services/eventService';
import { formatDate } from '../utils/formatDate';
import RegistrationForm from '../components/RegistrationForm';
import ParticipantList from '../components/ParticipantList';
import Spinner from '../components/Spinner';

export default function EventDetailPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = Number(id);

  const [event, setEvent] = useState<EventResponse | null>(null);
  const [participants, setParticipants] = useState<ParticipantResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(() => {
    Promise.all([getEventById(eventId), getParticipants(eventId)])
      .then(([eventData, participantData]) => {
        setEvent(eventData);
        setParticipants(participantData);
      })
      .catch(() => setError('Event not found'))
      .finally(() => setLoading(false));
  }, [eventId]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  if (loading) {
    return <Spinner className="py-16" />;
  }

  if (error || !event) {
    return (
      <div className="text-center py-12">
        <h2 className="text-lg font-medium text-gray-900">Event not found</h2>
        <p className="mt-1 text-sm text-gray-500">The event you are looking for does not exist.</p>
        <Link
          to="/"
          className="mt-4 inline-block text-sm font-medium text-indigo-600 hover:text-indigo-500"
        >
          &larr; Back to events
        </Link>
      </div>
    );
  }

  const isFull = event.availableSpots === 0;

  return (
    <div>
      <Link
        to="/"
        className="inline-flex items-center text-sm font-medium text-indigo-600 hover:text-indigo-500 transition-colors"
      >
        &larr; Back to events
      </Link>

      <div className="mt-4 rounded-lg bg-white p-6 shadow-sm border border-gray-200">
        <h1 className="text-2xl font-bold text-gray-900">{event.name}</h1>
        <p className="mt-1.5 text-sm text-gray-500">{formatDate(event.dateTime)}</p>
        <div className="mt-4 flex flex-wrap items-center gap-4 text-sm">
          <span className="text-gray-600">
            {event.participantCount} / {event.maxParticipants} participants
          </span>
          <span
            className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${
              isFull ? 'bg-red-100 text-red-700' : 'bg-green-100 text-green-700'
            }`}
          >
            {isFull ? 'Full' : `${event.availableSpots} spots left`}
          </span>
        </div>
      </div>

      <div className="mt-8 grid gap-8 md:grid-cols-2">
        <div>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Register</h2>
          {isFull ? (
            <div className="rounded-lg bg-red-50 border border-red-200 p-6 text-center">
              <p className="text-sm font-medium text-red-700">
                This event has reached maximum capacity.
              </p>
            </div>
          ) : (
            <RegistrationForm eventId={eventId} onSuccess={fetchData} disabled={isFull} />
          )}
        </div>

        <div>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            Participants ({participants.length})
          </h2>
          <ParticipantList participants={participants} />
        </div>
      </div>
    </div>
  );
}
