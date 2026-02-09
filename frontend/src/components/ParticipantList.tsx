import type { ParticipantResponse } from '../types/api';

interface ParticipantListProps {
  participants: ParticipantResponse[];
}

export default function ParticipantList({ participants }: ParticipantListProps) {
  if (participants.length === 0) {
    return (
      <div className="rounded-lg border border-dashed border-gray-300 p-6 text-center">
        <p className="text-sm text-gray-500">No participants registered yet.</p>
        <p className="mt-1 text-xs text-gray-400">Be the first to register!</p>
      </div>
    );
  }

  return (
    <div className="rounded-lg border border-gray-200 bg-white">
      <ul className="divide-y divide-gray-100">
        {participants.map((p, i) => (
          <li key={p.id} className="flex items-center justify-between px-4 py-3">
            <div className="flex items-center gap-3">
              <span className="flex h-7 w-7 items-center justify-center rounded-full bg-indigo-50 text-xs font-medium text-indigo-600">
                {i + 1}
              </span>
              <span className="text-sm text-gray-900">
                {p.firstName} {p.lastName}
              </span>
            </div>
            <span className="text-xs text-gray-400 font-mono">{p.personalCode}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}