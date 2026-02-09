export function formatDate(iso: string): string {
  return new Date(iso).toLocaleDateString('et-EE', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    timeZone: 'Europe/Tallinn',
  });
}