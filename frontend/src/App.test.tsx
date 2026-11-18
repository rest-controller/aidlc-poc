import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import App from './App';

function renderAt(path: string) {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <App />
    </MemoryRouter>,
  );
}

function mockStatus(status: number) {
  const fn = vi.fn().mockResolvedValue({ status });
  vi.stubGlobal('fetch', fn);
  return fn;
}

async function submit(username: string, password: string) {
  const user = userEvent.setup();
  if (username) await user.type(screen.getByTestId('username'), username);
  if (password) await user.type(screen.getByTestId('password'), password);
  await user.click(screen.getByTestId('submit'));
}

const FAILED = 'Invalid username or password';
const UNREACHABLE = 'Unable to reach the server. Please try again.';

describe('login flow', () => {
  beforeEach(() => {
    localStorage.clear();
    sessionStorage.clear();
  });
  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it('goes to the done screen on 200 and posts the typed values', async () => {
    const fetchMock = mockStatus(200);
    renderAt('/');
    await submit('alice', 'secret');
    expect(await screen.findByTestId('login-done')).toHaveTextContent(/^login done\.$/);
    const [url, init] = fetchMock.mock.calls[0];
    expect(url).toMatch(/\/api\/login$/);
    expect(init.method).toBe('POST');
    expect(JSON.parse(init.body)).toEqual({ username: 'alice', password: 'secret' });
  });

  it.each([401, 400])('shows the failure message and stays on the form for %i', async (code) => {
    mockStatus(code);
    renderAt('/');
    await submit('alice', 'wrong');
    expect(await screen.findByText(FAILED)).toBeInTheDocument();
    expect(screen.getByTestId('login-message')).toHaveTextContent(FAILED);
    expect(screen.getByTestId('submit')).toBeInTheDocument();
    expect(screen.queryByTestId('login-done')).not.toBeInTheDocument();
    expect(screen.getByTestId('username')).toHaveValue('alice');
    expect(screen.getByTestId('password')).toHaveValue('');
  });

  it('names no field in the failure message', async () => {
    mockStatus(401);
    renderAt('/');
    await submit('alice', 'wrong');
    const text = (await screen.findByText(FAILED)).textContent ?? '';
    expect(text).toBe(FAILED);
    expect(text.toLowerCase()).not.toMatch(/username is|password is|incorrect (username|password)/);
  });

  it('shows the unreachable message on a network error', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('Failed to fetch')));
    renderAt('/');
    await submit('alice', 'secret');
    expect(await screen.findByText(UNREACHABLE)).toBeInTheDocument();
    expect(screen.queryByTestId('login-done')).not.toBeInTheDocument();
  });

  it('shows the unreachable message on a 500', async () => {
    mockStatus(500);
    renderAt('/');
    await submit('alice', 'secret');
    expect(await screen.findByText(UNREACHABLE)).toBeInTheDocument();
  });

  it('disables submit while the request is in flight', async () => {
    let resolve!: (v: { status: number }) => void;
    vi.stubGlobal(
      'fetch',
      vi.fn().mockReturnValue(new Promise((r) => (resolve = r))),
    );
    renderAt('/');
    await submit('alice', 'secret');
    expect(screen.getByTestId('submit')).toBeDisabled();
    resolve({ status: 401 });
    await waitFor(() => expect(screen.getByTestId('submit')).toBeEnabled());
    expect(screen.getByTestId('login-message')).toHaveTextContent(FAILED);
  });

  it('sends empty values as they are', async () => {
    const fetchMock = mockStatus(401);
    renderAt('/');
    await submit('', '');
    await screen.findByText(FAILED);
    expect(JSON.parse(fetchMock.mock.calls[0][1].body)).toEqual({ username: '', password: '' });
  });

  it('renders only "login done." when /done is opened directly', () => {
    renderAt('/done');
    expect(screen.getByTestId('login-done')).toHaveTextContent(/^login done\.$/);
    expect(screen.queryByRole('button')).not.toBeInTheDocument();
    expect(screen.queryByRole('link')).not.toBeInTheDocument();
    expect(document.body.textContent).toBe('login done.');
  });

  it('writes nothing to local or session storage', async () => {
    mockStatus(401);
    renderAt('/');
    await submit('alice', 'secret');
    await screen.findByText(FAILED);
    expect(localStorage.length).toBe(0);
    expect(sessionStorage.length).toBe(0);
  });
});
