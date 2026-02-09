import { useEffect, useState } from 'react';
import type { EventResponse } from '../types/api';
import { getEvents } from '../services/eventService';
import EventCard from '../components/EventCard';

export default function EventListPage() {
  const [events, setEvents] = useState<EventResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getEvents()
      .then(setEvents)
      .catch(() => setError('Failed to load events'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p className="text-gray-500">Loading events...</p>;
  }

  if (error) {
    return <p className="text-red-600">{error}</p>;
  }

  if (events.length === 0) {
    return <p className="text-gray-500">No events yet.</p>;
  }

  return (
    <div>
      <h1 className="text-2xl font-bold text-gray-900 mb-6">Upcoming Events</h1>
      <div className="grid gap-4 sm:grid-cols-2">
        {events.map((event) => (
          <EventCard key={event.id} event={event} />
        ))}
      </div>
    </div>
  );
}