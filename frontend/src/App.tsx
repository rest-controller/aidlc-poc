import { Route, Routes } from 'react-router-dom';
import LoginPage from './LoginPage';
import DonePage from './DonePage';
import SignUpPage from './SignUpPage';

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/done" element={<DonePage />} />
      <Route path="/signup" element={<SignUpPage />} />
    </Routes>
  );
}
