import clsx from "clsx";

const toneStyles = {
  emerald: "border-emerald-400/40 bg-emerald-400/10 text-emerald-100",
  blue: "border-sky-400/40 bg-sky-400/10 text-sky-100",
  amber: "border-amber-400/40 bg-amber-400/10 text-amber-100",
  violet: "border-violet-400/40 bg-violet-400/10 text-violet-100",
  rose: "border-rose-400/40 bg-rose-400/10 text-rose-100",
};

export default function StatCard({
  label,
  value,
  description,
  tone = "emerald",
}: {
  label: string;
  value: number;
  description: string;
  tone?: keyof typeof toneStyles;
}) {
  return (
    <div
      className={clsx(
        "rounded-2xl border p-5 shadow-xl shadow-slate-950/40",
        toneStyles[tone]
      )}
    >
      <p className="text-sm uppercase tracking-[0.2em] text-slate-300">
        {label}
      </p>
      <p className="mt-4 text-4xl font-semibold">{value}</p>
      <p className="mt-2 text-sm text-slate-200/80">{description}</p>
    </div>
  );
}
