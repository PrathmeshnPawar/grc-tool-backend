import ResourceList, { type ListItem } from "@/components/ResourceList";
import Section from "@/components/Section";
import StatCard from "@/components/StatCard";
import {
  apiBaseUrl,
  fetchCollection,
  safeCount,
  type ApiCollection,
} from "@/lib/api";

type Risk = {
  id: string;
  title: string;
  description: string;
  status: string;
  riskScore: number;
};

type Policy = {
  id: string;
  title: string;
  description: string;
  status: string;
  version: string;
};

type Incident = {
  id: string;
  title: string;
  description: string;
  status: string;
  severity: string;
};

type Audit = {
  id: string;
  name: string;
  status: string;
  startDate?: string;
};

type Framework = {
  id: string;
  name: string;
  version: string;
};

type Control = {
  id: string;
  name: string;
  controlCode: string;
  status: string;
};

function mapToList<T extends { id: string }>(
  items: ApiCollection<T>,
  mapItem: (item: T) => ListItem
) {
  return items.slice(0, 4).map(mapItem);
}

async function fetchAll() {
  try {
    const [risks, policies, incidents, audits, frameworks] =
      await Promise.all([
        fetchCollection<Risk>("/api/risks"),
        fetchCollection<Policy>("/api/policies"),
        fetchCollection<Incident>("/api/incidents"),
        fetchCollection<Audit>("/api/audits"),
        fetchCollection<Framework>("/api/compliance/frameworks"),
      ]);

    const primaryFramework = frameworks[0];
    const controls = primaryFramework
      ? await fetchCollection<Control>(
          `/api/compliance/frameworks/${primaryFramework.id}/controls`
        )
      : [];

    return { risks, policies, incidents, audits, frameworks, controls };
  } catch (error) {
    return {
      risks: [],
      policies: [],
      incidents: [],
      audits: [],
      frameworks: [],
      controls: [],
      error: error instanceof Error ? error.message : "Unknown error",
    };
  }
}

export default async function HomePage() {
  const data = await fetchAll();

  return (
    <main className="mx-auto flex min-h-screen max-w-6xl flex-col gap-10 px-6 py-10">
      <header className="rounded-3xl border border-slate-800 bg-gradient-to-br from-slate-900 via-slate-950 to-slate-900 p-8">
        <div className="flex flex-wrap items-center justify-between gap-4">
          <div>
            <p className="text-sm uppercase tracking-[0.4em] text-slate-400">
              Governance, Risk & Compliance
            </p>
            <h1 className="mt-3 text-3xl font-semibold text-white">
              GRC Tool Command Center
            </h1>
            <p className="mt-2 max-w-xl text-sm text-slate-300">
              A Next.js 16-ready dashboard that surfaces the latest risk,
              policy, and audit activity coming from the Spring Boot API.
            </p>
          </div>
          <div className="rounded-2xl border border-slate-700 bg-slate-900/60 px-4 py-3 text-sm text-slate-300">
            <p className="text-xs uppercase tracking-[0.25em] text-slate-500">
              API Base URL
            </p>
            <p className="mt-1 font-medium text-slate-100">{apiBaseUrl}</p>
          </div>
        </div>
        {"error" in data ? (
          <div className="mt-6 rounded-2xl border border-rose-500/50 bg-rose-500/10 p-4 text-sm text-rose-100">
            <p className="font-semibold">Unable to load live API data.</p>
            <p className="mt-1 text-rose-200/80">{data.error}</p>
            <p className="mt-2 text-xs text-rose-200/70">
              Update NEXT_PUBLIC_API_BASE_URL in your environment once the
              backend is running.
            </p>
          </div>
        ) : null}
      </header>

      <section className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <StatCard
          label="Active Risks"
          value={safeCount(data.risks)}
          description="Open risk entries tracked in the system."
          tone="amber"
        />
        <StatCard
          label="Policies"
          value={safeCount(data.policies)}
          description="Published and draft policy catalog items."
          tone="blue"
        />
        <StatCard
          label="Incidents"
          value={safeCount(data.incidents)}
          description="Reported incidents awaiting response."
          tone="rose"
        />
        <StatCard
          label="Audits"
          value={safeCount(data.audits)}
          description="Audits in progress or recently completed."
          tone="violet"
        />
      </section>

      <div className="grid gap-6 lg:grid-cols-2">
        <Section
          title="Risk Register"
          description="Top scoring risks currently on the radar."
        >
          <ResourceList
            items={mapToList(data.risks, (risk) => ({
              id: risk.id,
              title: risk.title,
              subtitle: `Score ${risk.riskScore} · ${risk.description}`,
              status: risk.status,
            }))}
          />
        </Section>
        <Section
          title="Policy Tracker"
          description="Latest policy updates and approval status."
        >
          <ResourceList
            items={mapToList(data.policies, (policy) => ({
              id: policy.id,
              title: policy.title,
              subtitle: `v${policy.version} · ${policy.description}`,
              status: policy.status,
            }))}
          />
        </Section>
      </div>

      <div className="grid gap-6 lg:grid-cols-2">
        <Section
          title="Incident Response"
          description="Recent incident reports and severity levels."
        >
          <ResourceList
            items={mapToList(data.incidents, (incident) => ({
              id: incident.id,
              title: incident.title,
              subtitle: `${incident.severity} · ${incident.description}`,
              status: incident.status,
            }))}
          />
        </Section>
        <Section
          title="Audit Operations"
          description="Audit cadence and execution progress."
        >
          <ResourceList
            items={mapToList(data.audits, (audit) => ({
              id: audit.id,
              title: audit.name,
              subtitle: audit.startDate
                ? `Starts ${audit.startDate}`
                : "Upcoming audit",
              status: audit.status,
            }))}
          />
        </Section>
      </div>

      <div className="grid gap-6 lg:grid-cols-2">
        <Section
          title="Compliance Frameworks"
          description="Frameworks and standards connected to controls."
        >
          <ResourceList
            items={mapToList(data.frameworks, (framework) => ({
              id: framework.id,
              title: framework.name,
              subtitle: `Version ${framework.version}`,
            }))}
          />
        </Section>
        <Section
          title="Control Library"
          description="Controls mapped to compliance requirements."
        >
          <ResourceList
            items={mapToList(data.controls, (control) => ({
              id: control.id,
              title: control.name,
              subtitle: `Code ${control.controlCode}`,
              status: control.status,
            }))}
          />
        </Section>
      </div>
    </main>
  );
}
