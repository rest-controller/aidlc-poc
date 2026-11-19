export type SignupResult =
  | { kind: 'created' }
  | { kind: 'invalid'; errors: string[] }
  | { kind: 'taken' }
  | { kind: 'unreachable' };

const API_BASE: string = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

/** Reads the reason codes of a 400 (contract C1); a missing or unreadable body gives none. */
async function readErrors(response: Response): Promise<string[]> {
  try {
    const body: unknown = await response.json();
    const errors = (body as { errors?: unknown } | null)?.errors;
    if (Array.isArray(errors)) {
      return errors.filter((code): code is string => typeof code === 'string');
    }
  } catch {
    // No JSON body: treated as a 400 without codes.
  }
  return [];
}

/**
 * Posts the typed values to the backend and reduces the answer to a result.
 * The status code decides the result; only the reason codes of a 400 are read from the body,
 * and the password is never logged.
 */
export async function signup(username: string, password: string): Promise<SignupResult> {
  try {
    const response = await fetch(`${API_BASE}/api/signup`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    if (response.status === 201) return { kind: 'created' };
    if (response.status === 400) return { kind: 'invalid', errors: await readErrors(response) };
    if (response.status === 409) return { kind: 'taken' };
    return { kind: 'unreachable' };
  } catch {
    return { kind: 'unreachable' };
  }
}
