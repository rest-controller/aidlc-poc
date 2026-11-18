export type SignupResult = 'created' | 'invalid' | 'taken' | 'unreachable';

const API_BASE: string = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

/**
 * Posts the typed values to the backend and reduces the answer to a result.
 * Only the status code is read; the response body is never used and the password is never logged.
 */
export async function signup(username: string, password: string): Promise<SignupResult> {
  try {
    const response = await fetch(`${API_BASE}/api/signup`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    if (response.status === 201) return 'created';
    if (response.status === 400) return 'invalid';
    if (response.status === 409) return 'taken';
    return 'unreachable';
  } catch {
    return 'unreachable';
  }
}
