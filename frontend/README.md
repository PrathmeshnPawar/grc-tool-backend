# GRC Tool Frontend

This is a Next.js dashboard that connects to the Spring Boot API in this
repository. It surfaces high-level counts for risks, incidents, audits, and
policies, plus recent records per module.

## Getting Started

```bash
cd frontend
npm install
npm run dev
```

Set the backend URL with an environment variable if needed:

```bash
export NEXT_PUBLIC_API_BASE_URL="http://localhost:8080"
```

## Notes

The compliance controls list pulls controls for the first compliance framework
returned by the API. Add more frameworks/controls on the backend to populate the
sections.
