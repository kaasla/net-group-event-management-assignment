import { useEffect, useState, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import type { EventResponse, ParticipantResponse } from '../types/api';
import { getEventById, getParticipants } from '../services/eventService';
import { formatDate } from '../utils/formatDate';
import RegistrationForm from '../components/RegistrationForm';
import ParticipantList from '../components/ParticipantList';

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
    return <p className="text-gray-500">Loading event...</p>;
  }

  if (error || !event) {
    return (
      <div>
        <p className="text-red-600">{error ?? 'Event not found'}</p>
        <Link to="/" className="mt-2 inline-block text-sm text-indigo-600 hover:text-indigo-500">
          Back to events
        </Link>
      </div>
    );
  }

  const isFull = event.availableSpots === 0;

  return (
    <div>
      <Link to="/" className="text-sm text-indigo-600 hover:text-indigo-500">
        &larr; Back to events
      </Link>

      <div className="mt-4 rounded-lg bg-white p-6 shadow">
        <h1 className="text-2xl font-bold text-gray-900">{event.name}</h1>
        <p className="mt-1 text-sm text-gray-500">{formatDate(event.dateTime)}</p>
        <div className="mt-4 flex gap-6 text-sm">
          <span className="text-gray-700">
            {event.participantCount} / {event.maxParticipants} participants
          </span>
          <span className={isFull ? 'text-red-600' : 'text-green-600'}>
            {isFull ? 'Full' : `${event.availableSpots} spots left`}
          </span>
        </div>
      </div>

      <div className="mt-8 grid gap-8 md:grid-cols-2">
        <div>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Register</h2>
          {isFull ? (
            <p className="text-sm text-red-600">This event has reached maximum capacity.</p>
          ) : (
            <RegistrationForm
              eventId={eventId}
              onSuccess={fetchData}
              disabled={isFull}
            />
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