import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "GRC Tool Dashboard",
  description: "Front-end overview for the GRC Tool backend.",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="bg-slate-950 text-slate-100 antialiased">
        {children}
      </body>
    </html>
  );
}
