import { useEffect } from 'react';

interface AlertProps {
  message: string;
  type: 'success' | 'error';
  onDismiss?: () => void;
  autoDismissMs?: number;
}

export default function Alert({ message, type, onDismiss, autoDismissMs = 4000 }: AlertProps) {
  useEffect(() => {
    if (!onDismiss) return;
    const timer = setTimeout(onDismiss, autoDismissMs);
    return () => clearTimeout(timer);
  }, [onDismiss, autoDismissMs]);

  const styles = type === 'success'
    ? 'bg-green-50 border-green-200 text-green-800'
    : 'bg-red-50 border-red-200 text-red-800';

  return (
    <div className={`rounded-md border px-4 py-3 text-sm ${styles}`}>
      {message}
    </div>
  );
}