import { useState } from 'react';
import type { RegistrationRequest, ErrorResponse } from '../types/api';
import { registerParticipant } from '../services/eventService';
import Alert from './Alert';
import axios from 'axios';

interface RegistrationFormProps {
  eventId: number;
  onSuccess: () => void;
  disabled: boolean;
}

export default function RegistrationForm({ eventId, onSuccess, disabled }: RegistrationFormProps) {
  const [form, setForm] = useState<RegistrationRequest>({
    firstName: '',
    lastName: '',
    personalCode: '',
  });
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setFieldErrors((prev) => ({ ...prev, [e.target.name]: '' }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setFieldErrors({});
    setSuccess(false);
    setSubmitting(true);

    registerParticipant(eventId, form)
      .then(() => {
        setForm({ firstName: '', lastName: '', personalCode: '' });
        setSuccess(true);
        onSuccess();
      })
      .catch((err: unknown) => {
        if (axios.isAxiosError(err) && err.response) {
          const data = err.response.data as ErrorResponse;
          if (data.fieldErrors) {
            const mapped: Record<string, string> = {};
            data.fieldErrors.forEach((fe) => { mapped[fe.field] = fe.message; });
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

  const inputClass = (field: string) =>
    `mt-1 block w-full rounded-md border px-3 py-2 text-sm shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 disabled:bg-gray-100 ${
      fieldErrors[field] ? 'border-red-300' : 'border-gray-300'
    }`;

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {success && (
        <Alert
          message="Registration successful!"
          type="success"
          onDismiss={() => setSuccess(false)}
        />
      )}

      <div>
        <label htmlFor="firstName" className="block text-sm font-medium text-gray-700">
          First Name
        </label>
        <input
          id="firstName"
          name="firstName"
          value={form.firstName}
          onChange={handleChange}
          disabled={disabled || submitting}
          className={inputClass('firstName')}
        />
        {fieldErrors.firstName && (
          <p className="mt-1 text-xs text-red-600">{fieldErrors.firstName}</p>
        )}
      </div>

      <div>
        <label htmlFor="lastName" className="block text-sm font-medium text-gray-700">
          Last Name
        </label>
        <input
          id="lastName"
          name="lastName"
          value={form.lastName}
          onChange={handleChange}
          disabled={disabled || submitting}
          className={inputClass('lastName')}
        />
        {fieldErrors.lastName && (
          <p className="mt-1 text-xs text-red-600">{fieldErrors.lastName}</p>
        )}
      </div>

      <div>
        <label htmlFor="personalCode" className="block text-sm font-medium text-gray-700">
          Personal Code
        </label>
        <input
          id="personalCode"
          name="personalCode"
          value={form.personalCode}
          onChange={handleChange}
          disabled={disabled || submitting}
          placeholder="e.g. 49403136515"
          className={inputClass('personalCode')}
        />
        {fieldErrors.personalCode && (
          <p className="mt-1 text-xs text-red-600">{fieldErrors.personalCode}</p>
        )}
      </div>

      {error && <Alert message={error} type="error" />}

      <button
        type="submit"
        disabled={disabled || submitting}
        className="w-full rounded-md bg-indigo-600 px-4 py-2.5 text-sm font-medium text-white hover:bg-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors"
      >
        {submitting ? 'Registering...' : 'Register'}
      </button>
    </form>
  );
}