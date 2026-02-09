import { BrowserRouter, Routes, Route } from 'react-router-dom';
import EventListPage from './pages/EventListPage';
import EventDetailPage from './pages/EventDetailPage';
import LoginPage from './pages/LoginPage';

export default function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-gray-50">
        <Routes>
          <Route path="/" element={<EventListPage />} />
          <Route path="/events/:id" element={<EventDetailPage />} />
          <Route path="/login" element={<LoginPage />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}