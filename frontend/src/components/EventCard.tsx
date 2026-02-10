import { Link } from 'react-router-dom';
import type { EventResponse } from '../types/api';
import { formatDate } from '../utils/formatDate';

interface EventCardProps {
  event: EventResponse;
}

export default function EventCard({ event }: EventCardProps) {
  const isFull = event.availableSpots === 0;

  return (
    <Link
      to={`/events/${event.id}`}
      className="group block rounded-lg bg-white p-6 shadow-sm border border-gray-200 hover:shadow-md hover:border-indigo-200 transition-all"
    >
      <h2 className="text-lg font-semibold text-gray-900 group-hover:text-indigo-600 transition-colors truncate">
        {event.name}
      </h2>
      <p className="mt-1.5 text-sm text-gray-500">{formatDate(event.dateTime)}</p>
      <div className="mt-4 flex items-center justify-between text-sm">
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
    </Link>
  );
}
