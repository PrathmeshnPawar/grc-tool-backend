import clsx from "clsx";

export type ListItem = {
  id: string;
  title: string;
  subtitle?: string;
  status?: string;
};

const statusStyles: Record<string, string> = {
  OPEN: "bg-emerald-400/20 text-emerald-200",
  IN_PROGRESS: "bg-sky-400/20 text-sky-200",
  CLOSED: "bg-slate-400/20 text-slate-200",
  ACTIVE: "bg-emerald-400/20 text-emerald-200",
  DRAFT: "bg-amber-400/20 text-amber-200",
  ARCHIVED: "bg-rose-400/20 text-rose-200",
};

export default function ResourceList({ items }: { items: ListItem[] }) {
  if (items.length === 0) {
    return (
      <div className="rounded-xl border border-dashed border-slate-700 p-6 text-center text-sm text-slate-400">
        No data available yet. Add a few records from the API to see them
        here.
      </div>
    );
  }

  return (
    <ul className="divide-y divide-slate-800 rounded-xl border border-slate-800">
      {items.map((item) => (
        <li key={item.id} className="flex flex-wrap items-center gap-3 p-4">
          <div className="min-w-0 flex-1">
            <p className="truncate text-sm font-semibold text-slate-100">
              {item.title}
            </p>
            {item.subtitle ? (
              <p className="mt-1 text-xs text-slate-400">{item.subtitle}</p>
            ) : null}
          </div>
          {item.status ? (
            <span
              className={clsx(
                "rounded-full px-3 py-1 text-xs font-semibold",
                statusStyles[item.status] ?? "bg-slate-700 text-slate-200"
              )}
            >
              {item.status.replace(/_/g, " ")}
            </span>
          ) : null}
        </li>
      ))}
    </ul>
  );
}
