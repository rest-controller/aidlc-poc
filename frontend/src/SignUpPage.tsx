import { FormEvent, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signup } from './signupApi';

type Status = 'idle' | 'submitting';

export default function SignUpPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [status, setStatus] = useState<Status>('idle');
  const inFlight = useRef(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    // One request per submit (BR1.4): the ref is read synchronously, so a fast double submit
    // cannot slip through before the page re-renders.
    if (inFlight.current) return;
    inFlight.current = true;
    setStatus('submitting');
    try {
      const result = await signup(username, password);
      if (result === 'created') {
        // One-time flag in router state; LoginPage shows the message once (BR1.5).
        navigate('/', { state: { signedUp: true } });
        return;
      }
      // Messages for the other answers arrive in later tickets; the line stays empty for now.
    } finally {
      inFlight.current = false;
      setStatus('idle');
    }
  }

  return (
    <main>
      <h1>Sign up</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Username
          <input
            data-testid="signup-username"
            type="text"
            value={username}
            aria-describedby="signup-username-hint"
            onChange={(e) => setUsername(e.target.value)}
          />
        </label>
        <p id="signup-username-hint">3-20 letters, digits, underscores or hyphens</p>
        <label>
          Password
          <input
            data-testid="signup-password"
            type="password"
            value={password}
            aria-describedby="signup-password-hint"
            onChange={(e) => setPassword(e.target.value)}
          />
        </label>
        <p id="signup-password-hint">At least 8 characters</p>
        <button data-testid="signup-submit" type="submit" disabled={status === 'submitting'}>
          Sign up
        </button>
      </form>
      <p data-testid="signup-message" role="alert"></p>
    </main>
  );
}
