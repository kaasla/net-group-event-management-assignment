import { Link } from 'react-router-dom';

export default function Navbar() {
  return (
    <nav className="bg-white shadow">
      <div className="mx-auto max-w-5xl px-4 py-4 flex items-center justify-between">
        <Link to="/" className="text-xl font-bold text-gray-900">
          Event Management
        </Link>
        <Link
          to="/login"
          className="text-sm font-medium text-indigo-600 hover:text-indigo-500"
        >
          Admin Login
        </Link>
      </div>
    </nav>
  );
}