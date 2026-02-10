import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { createEvent } from '../services/eventService';
import type { ErrorResponse } from '../types/api';
import Alert from '../components/Alert';
import axios from 'axios';

export default function CreateEventPage() {
  const { isAdmin } = useAuth();
  const navigate = useNavigate();

  const [name, setName] = useState('');
  const [dateTime, setDateTime] = useState('');
  const [maxParticipants, setMaxParticipants] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (!isAdmin) {
      navigate('/login');
    }
  }, [isAdmin, navigate]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setFieldErrors({});
    setSubmitting(true);

    createEvent({
      name,
      dateTime: new Date(dateTime).toISOString(),
      maxParticipants: Number(maxParticipants),
    })
      .then((event) => navigate(`/events/${event.id}`))
      .catch((err: unknown) => {
        if (axios.isAxiosError(err) && err.response) {
          const data = err.response.data as ErrorResponse;
          if (data.fieldErrors) {
            const mapped: Record<string, string> = {};
            data.fieldErrors.forEach((fe) => {
              mapped[fe.field] = fe.message;
            });
            setFieldErrors(mapped);
          } else {
            setError(data.message);
          }
        } else {
          setError('An unexpected error occurred');
        }
      })
      .finally(() => setSubmitting(false));
  };

  if (!isAdmin) {
    return null;
  }

  const inputClass = (field: string) =>
    `mt-1 block w-full rounded-md border px-3 py-2 text-sm shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 disabled:bg-gray-100 ${
      fieldErrors[field] ? 'border-red-300' : 'border-gray-300'
    }`;

  return (
    <div className="mx-auto max-w-lg">
      <Link
        to="/"
        className="inline-flex items-center text-sm font-medium text-indigo-600 hover:text-indigo-500 transition-colors"
      >
        &larr; Back to events
      </Link>

      <div className="mt-4 rounded-lg bg-white p-6 shadow-sm border border-gray-200">
        <h1 className="text-2xl font-bold text-gray-900 mb-6">Create Event</h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700">
              Event Name
            </label>
            <input
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              disabled={submitting}
              className={inputClass('name')}
            />
            {fieldErrors.name && <p className="mt-1 text-xs text-red-600">{fieldErrors.name}</p>}
          </div>

          <div>
            <label htmlFor="dateTime" className="block text-sm font-medium text-gray-700">
              Date and Time
            </label>
            <input
              id="dateTime"
              type="datetime-local"
              value={dateTime}
              onChange={(e) => setDateTime(e.target.value)}
              disabled={submitting}
              className={inputClass('dateTime')}
            />
            {fieldErrors.dateTime && (
              <p className="mt-1 text-xs text-red-600">{fieldErrors.dateTime}</p>
            )}
          </div>

          <div>
            <label htmlFor="maxParticipants" className="block text-sm font-medium text-gray-700">
              Maximum Participants
            </label>
            <input
              id="maxParticipants"
              type="number"
              min="1"
              value={maxParticipants}
              onChange={(e) => setMaxParticipants(e.target.value)}
              disabled={submitting}
              className={inputClass('maxParticipants')}
            />
            {fieldErrors.maxParticipants && (
              <p className="mt-1 text-xs text-red-600">{fieldErrors.maxParticipants}</p>
            )}
          </div>

          {error && <Alert message={error} type="error" />}

          <button
            type="submit"
            disabled={submitting}
            className="w-full rounded-md bg-indigo-600 px-4 py-2.5 text-sm font-medium text-white hover:bg-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors"
          >
            {submitting ? 'Creating...' : 'Create Event'}
          </button>
        </form>
      </div>
    </div>
  );
}
