export type LoginResult = 'success' | 'failed' | 'unreachable';

const API_BASE: string = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

/**
 * Posts the typed values to the backend and reduces the answer to a result.
 * Only the status code is read; the response body is never used.
 */
export async function login(username: string, password: string): Promise<LoginResult> {
  try {
    const response = await fetch(`${API_BASE}/api/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    if (response.status === 200) return 'success';
    if (response.status === 401 || response.status === 400) return 'failed';
    return 'unreachable';
  } catch {
    return 'unreachable';
  }
}
