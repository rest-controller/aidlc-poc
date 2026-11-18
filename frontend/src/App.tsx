import { Route, Routes } from 'react-router-dom';
import LoginPage from './LoginPage';
import DonePage from './DonePage';

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/done" element={<DonePage />} />
    </Routes>
  );
}
