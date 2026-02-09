import { Link } from 'react-router-dom';
import type { EventResponse } from '../types/api';
import { formatDate } from '../utils/formatDate';

interface EventCardProps {
  event: EventResponse;
}

export default function EventCard({ event }: EventCardProps) {
  return (
    <Link
      to={`/events/${event.id}`}
      className="block rounded-lg bg-white p-6 shadow hover:shadow-md transition-shadow"
    >
      <h2 className="text-lg font-semibold text-gray-900">{event.name}</h2>
      <p className="mt-1 text-sm text-gray-500">{formatDate(event.dateTime)}</p>
      <div className="mt-4 flex items-center gap-4 text-sm">
        <span className="text-gray-700">
          {event.participantCount} / {event.maxParticipants} participants
        </span>
        <span
          className={
            event.availableSpots > 0 ? 'text-green-600' : 'text-red-600'
          }
        >
          {event.availableSpots > 0
            ? `${event.availableSpots} spots left`
            : 'Full'}
        </span>
      </div>
    </Link>
  );
}