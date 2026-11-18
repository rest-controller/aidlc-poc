import '@testing-library/jest-dom/vitest';

// Newer Node versions expose an experimental global `localStorage` that shadows
// jsdom's. Install in-memory Storage implementations so tests behave the same
// on every Node version.
function memoryStorage(): Storage {
  const data = new Map<string, string>();
  return {
    get length() {
      return data.size;
    },
    clear: () => data.clear(),
    getItem: (key) => (data.has(key) ? (data.get(key) as string) : null),
    key: (index) => Array.from(data.keys())[index] ?? null,
    removeItem: (key) => {
      data.delete(key);
    },
    setItem: (key, value) => {
      data.set(key, String(value));
    },
  };
}

for (const name of ['localStorage', 'sessionStorage'] as const) {
  Object.defineProperty(globalThis, name, {
    value: memoryStorage(),
    configurable: true,
    writable: true,
  });
}
