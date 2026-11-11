# Sushi Shop — Frontend

React 19 + TypeScript SPA for the Sushi Shop storefront and admin panel,
built with Vite and Tailwind CSS.

## Requirements

- Node.js **24** (same version as CI and the Docker image)
- Backend running on `http://localhost:8080` (see the [root README](../README.md))

## Getting started

```bash
npm ci
npm run dev        # http://localhost:5173
```

The dev server proxies `/api` (REST) and `/ws` (WebSocket / STOMP) to the
backend, so no extra env configuration is needed locally.

## Scripts

| Command           | Description                                 |
| ----------------- | ------------------------------------------- |
| `npm run dev`     | Start Vite dev server with HMR              |
| `npm run build`   | Type-check (`tsc -b`) and build to `dist/`  |
| `npm run lint`    | Run ESLint                                  |
| `npm run preview` | Serve the production build locally          |

## Project structure

```
src/
├── api/          # Axios client + one module per backend resource
├── components/   # Reusable UI, grouped by feature (Home, Product, Admin, UI…)
├── context/      # Auth, Cart and Notification providers
├── hooks/
│   ├── common/   # Generic hooks (useApi, usePagination, useTheme…)
│   └── features/ # Domain hooks (useProducts, useOrders, useAudit…)
├── layout/       # Page shells (header / footer)
├── pages/        # Route-level components
├── styles/       # Global CSS variables
└── types/        # DTO types mirroring backend responses
```

### Conventions

- Components use **CSS Modules** (`*.module.css`) next to the `.tsx` file.
- All HTTP calls go through `api/client.ts`, which attaches the JWT and
  redirects to `/login` on `401`.
- Data fetching in components goes through `useApi` (or a feature hook built
  on it) to get consistent loading / error handling and toasts.

## Docker

The production image is a two-stage build: Node builds the static assets,
then nginx serves them and proxies `/api` and `/ws` to the backend
(see `nginx.conf`).

```bash
docker build -t sushi-frontend .
```

Images are built and published by the **Frontend** GitHub Actions workflow
on every push to `main`.
