import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

export default function Navbar() {
  const { isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="bg-white shadow">
      <div className="mx-auto max-w-5xl px-4 py-4 flex items-center justify-between">
        <Link to="/" className="text-xl font-bold text-gray-900">
          Event Management
        </Link>
        <div className="flex items-center gap-4">
          {isAdmin && (
            <Link
              to="/events/new"
              className="text-sm font-medium text-indigo-600 hover:text-indigo-500"
            >
              Create Event
            </Link>
          )}
          {isAdmin ? (
            <button
              onClick={handleLogout}
              className="text-sm font-medium text-gray-600 hover:text-gray-500"
            >
              Logout
            </button>
          ) : (
            <Link
              to="/login"
              className="text-sm font-medium text-indigo-600 hover:text-indigo-500"
            >
              Admin Login
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
}