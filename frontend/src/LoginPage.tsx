import { FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from './loginApi';

type Status = 'idle' | 'submitting' | 'failed' | 'unreachable';

const MESSAGES: Record<Status, string> = {
  idle: '',
  submitting: '',
  failed: 'Invalid username or password',
  unreachable: 'Unable to reach the server. Please try again.',
};

export default function LoginPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [status, setStatus] = useState<Status>('idle');

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    if (status === 'submitting') return;
    setStatus('submitting'); // also clears the old message
    const result = await login(username, password);
    if (result === 'success') {
      navigate('/done');
      return;
    }
    if (result === 'failed') setPassword('');
    setStatus(result);
  }

  return (
    <main>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Username
          <input
            data-testid="username"
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </label>
        <label>
          Password
          <input
            data-testid="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </label>
        <button data-testid="submit" type="submit" disabled={status === 'submitting'}>
          Log in
        </button>
      </form>
      <p data-testid="login-message" role="alert">
        {MESSAGES[status]}
      </p>
    </main>
  );
}
