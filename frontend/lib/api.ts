const defaultBaseUrl = "http://localhost:8080";

export const apiBaseUrl =
  process.env.NEXT_PUBLIC_API_BASE_URL ?? defaultBaseUrl;

export type ApiCollection<T> = T[];

export type ResourceSummary = {
  id: string;
  title: string;
  subtitle?: string;
  status?: string;
};

export async function fetchCollection<T>(path: string): Promise<ApiCollection<T>> {
  const response = await fetch(`${apiBaseUrl}${path}`, {
    cache: "no-store",
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch ${path}: ${response.status}`);
  }

  return (await response.json()) as ApiCollection<T>;
}

export function safeCount<T>(collection?: ApiCollection<T>) {
  return collection?.length ?? 0;
}
