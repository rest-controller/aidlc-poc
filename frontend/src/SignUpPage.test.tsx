import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter, useLocation } from 'react-router-dom';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import App from './App';

// Shows the router's current path and navigation state so tests can see the replaced entry.
function LocationProbe() {
  const location = useLocation();
  return (
    <div
      data-testid="probe"
      data-path={location.pathname}
      data-state={JSON.stringify(location.state)}
    />
  );
}

function renderAt(path: string) {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <App />
      <LocationProbe />
    </MemoryRouter>,
  );
}

async function fillAndSubmit(username: string, password: string) {
  const user = userEvent.setup();
  await user.type(screen.getByTestId('signup-username'), username);
  await user.type(screen.getByTestId('signup-password'), password);
  await user.click(screen.getByTestId('signup-submit'));
}

const DONE = 'Sign-up done. You can log in now.';

describe('sign-up flow', () => {
  beforeEach(() => {
    localStorage.clear();
    sessionStorage.clear();
  });
  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it('posts the typed values and goes to the login page with the one-time message on 201', async () => {
    const fetchMock = vi.fn().mockResolvedValue({ status: 201 });
    vi.stubGlobal('fetch', fetchMock);
    renderAt('/signup');
    await fillAndSubmit('alice', 'secret-pass');
    expect(await screen.findByTestId('login-message')).toHaveTextContent(DONE);
    expect(screen.getByRole('heading', { name: 'Login' })).toBeInTheDocument();
    expect(screen.queryByTestId('login-done')).not.toBeInTheDocument();
    const [url, init] = fetchMock.mock.calls[0];
    expect(url).toMatch(/\/api\/signup$/);
    expect(init.method).toBe('POST');
    expect(JSON.parse(init.body)).toEqual({ username: 'alice', password: 'secret-pass' });
  });

  it('removes the flag by replacing the history entry, so a reload shows nothing', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ status: 201 }));
    renderAt('/signup');
    await fillAndSubmit('alice', 'secret-pass');
    await screen.findByText(DONE);
    await waitFor(() => expect(screen.getByTestId('probe').dataset.state).toBe('null'));
    expect(screen.getByTestId('probe').dataset.path).toBe('/');
    // A fresh visit to "/" (what a reload gives) has no flag and an empty message line.
    document.body.innerHTML = '';
    renderAt('/');
    expect(screen.getByTestId('login-message')).toBeEmptyDOMElement();
  });

  it('disables the button while the request is pending', async () => {
    let resolve!: (v: { status: number }) => void;
    vi.stubGlobal('fetch', vi.fn().mockReturnValue(new Promise((r) => (resolve = r))));
    renderAt('/signup');
    await fillAndSubmit('alice', 'secret-pass');
    expect(screen.getByTestId('signup-submit')).toBeDisabled();
    resolve({ status: 409 });
    await waitFor(() => expect(screen.getByTestId('signup-submit')).toBeEnabled());
    expect(screen.getByTestId('signup-message')).toBeEmptyDOMElement();
  });

  it('re-enables the button and stays on /signup when the backend is stopped', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('Failed to fetch')));
    renderAt('/signup');
    await fillAndSubmit('alice', 'secret-pass');
    await waitFor(() => expect(screen.getByTestId('signup-submit')).toBeEnabled());
    expect(screen.getByTestId('probe').dataset.path).toBe('/signup');
    expect(screen.getByRole('heading', { name: 'Sign up' })).toBeInTheDocument();
  });

  it('stays on /signup for a 400 and a 500 answer', async () => {
    for (const status of [400, 500]) {
      vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ status }));
      const { unmount } = renderAt('/signup');
      await fillAndSubmit('alice', 'secret-pass');
      await waitFor(() => expect(screen.getByTestId('signup-submit')).toBeEnabled());
      expect(screen.getByTestId('probe').dataset.path).toBe('/signup');
      unmount();
    }
  });

  it('has labelled fields with hints tied to them', () => {
    renderAt('/signup');
    const username = screen.getByLabelText('Username');
    const password = screen.getByLabelText('Password');
    expect(username).toHaveAccessibleDescription('3-20 letters, digits, underscores or hyphens');
    expect(password).toHaveAccessibleDescription('At least 8 characters');
    expect(screen.getByRole('button', { name: 'Sign up' })).toBeInTheDocument();
    expect(screen.getByTestId('signup-message')).toHaveAttribute('role', 'alert');
  });

  it('writes nothing to local or session storage', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ status: 201 }));
    renderAt('/signup');
    await fillAndSubmit('alice', 'secret-pass');
    await screen.findByText(DONE);
    expect(localStorage.length).toBe(0);
    expect(sessionStorage.length).toBe(0);
  });
});
