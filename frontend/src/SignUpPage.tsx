import { FormEvent, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signup } from './signupApi';

type Status = 'idle' | 'submitting';

// Wording table of contract C1.
const USERNAME_MESSAGE = 'Username must be 3-20 letters, digits, underscores or hyphens.';
const PASSWORD_MESSAGE = 'Password must be at least 8 characters.';
const TAKEN_MESSAGE = 'Username already taken.';
const UNREACHABLE_MESSAGE = 'Unable to reach the server. Please try again.';
const GENERIC_MESSAGE = 'Please check the username and password and try again.';

function messageFor(errors: string[]): string {
  const parts: string[] = [];
  if (errors.includes('USERNAME_INVALID')) parts.push(USERNAME_MESSAGE);
  if (errors.includes('PASSWORD_INVALID')) parts.push(PASSWORD_MESSAGE);
  return parts.length > 0 ? parts.join(' ') : GENERIC_MESSAGE;
}

export default function SignUpPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [status, setStatus] = useState<Status>('idle');
  const [message, setMessage] = useState('');
  const inFlight = useRef(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    // One request per submit (BR1.4): the ref is read synchronously, so a fast double submit
    // cannot slip through before the page re-renders.
    if (inFlight.current) return;
    inFlight.current = true;
    setStatus('submitting');
    try {
      setMessage('');
      const result = await signup(username, password);
      if (result.kind === 'created') {
        // One-time flag in router state; LoginPage shows the message once (BR1.5).
        navigate('/', { state: { signedUp: true } });
        return;
      }
      if (result.kind === 'invalid') {
        // FR2.4: the username stays so it can be fixed; the password is cleared.
        setMessage(messageFor(result.errors));
        setPassword('');
      }
      if (result.kind === 'taken') {
        // AC2.3.4: same handling as a failed rule: username kept, password cleared.
        setMessage(TAKEN_MESSAGE);
        setPassword('');
      }
      if (result.kind === 'unreachable') {
        // FR4.3: network error or unexpected status. Both fields stay as typed so a retry is one click.
        setMessage(UNREACHABLE_MESSAGE);
      }
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
      <p data-testid="signup-message" role="alert">
        {message}
      </p>
    </main>
  );
}
