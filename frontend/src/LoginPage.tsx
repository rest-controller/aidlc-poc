import { FormEvent, useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { login } from './loginApi';

type Status = 'idle' | 'submitting' | 'failed' | 'unreachable';

const MESSAGES: Record<Status, string> = {
  idle: '',
  submitting: '',
  failed: 'Invalid username or password',
  unreachable: 'Unable to reach the server. Please try again.',
};

const SIGNED_UP_MESSAGE = 'Sign-up done. You can log in now.';

export default function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [status, setStatus] = useState<Status>('idle');
  // One-time flag from a successful sign-up, read once from router state (BR1.5).
  const [signedUp, setSignedUp] = useState(
    () => (location.state as { signedUp?: boolean } | null)?.signedUp === true,
  );

  useEffect(() => {
    // Replace the history entry so a reload shows nothing; the message stays in this state.
    if (signedUp && location.state) navigate('/', { replace: true, state: null });
  }, [signedUp, location.state, navigate]);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    if (status === 'submitting') return;
    setSignedUp(false);
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
        {status === 'idle' && signedUp ? SIGNED_UP_MESSAGE : MESSAGES[status]}
      </p>
      <Link to="/signup">Sign up</Link>
    </main>
  );
}
