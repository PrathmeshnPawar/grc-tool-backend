/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        "grc-slate": "#0f172a",
        "grc-teal": "#14b8a6",
      },
    },
  },
  plugins: [],
};
