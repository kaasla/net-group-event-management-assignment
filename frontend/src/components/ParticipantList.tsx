import type { ParticipantResponse } from '../types/api';

interface ParticipantListProps {
  participants: ParticipantResponse[];
}

export default function ParticipantList({ participants }: ParticipantListProps) {
  if (participants.length === 0) {
    return <p className="text-sm text-gray-500">No participants yet.</p>;
  }

  return (
    <ul className="divide-y divide-gray-200">
      {participants.map((p) => (
        <li key={p.id} className="py-3 flex items-center justify-between">
          <span className="text-sm text-gray-900">
            {p.firstName} {p.lastName}
          </span>
          <span className="text-sm text-gray-500 font-mono">{p.personalCode}</span>
        </li>
      ))}
    </ul>
  );
}