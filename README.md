# LoadShed Hub SA backend

Spring Boot 3 / Java 21 API for the LoadShed Hub SA community power map.

## Run locally

Requires a local MySQL instance (default: `loadshed_hub` database, `root`/`sandile` — override with env vars below).

```bash
mvn spring-boot:run
```

The app seeds demo data on first run (`app.seed-data=true` by default): a citizen, a hub owner, and an admin account, each with password `Password123`.

- `citizen@example.com`
- `owner@example.com`
- `admin@example.com`

## Configuration (environment variables)

All of these have safe local-dev defaults in `application.properties`; override them for any real deployment.

| Variable | Purpose |
|---|---|
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | MySQL connection |
| `JWT_SECRET` | HMAC signing key for auth tokens — **must** be overridden in production |
| `JWT_EXPIRATION_MS` | Token lifetime in milliseconds |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of browser origins allowed to call the API |
| `FRONTEND_URL` | Base URL used to build password-reset links |
| `MAIL_ENABLED`, `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD` | Outgoing email for password resets. When `MAIL_ENABLED` is unset/false, reset links are logged to the console instead of emailed — handy for local dev |
| `SEED_DATA` | Set to `false` to skip demo-data seeding |
| `DDL_AUTO` | Hibernate schema mode (`update` locally; use the `prod` profile's `validate` + Flyway in production) |

## Production profile

Run with `SPRING_PROFILES_ACTIVE=prod` to apply `application-prod.properties`, which:
- Uses Flyway migrations instead of Hibernate auto-DDL
- Disables demo data seeding
- Disables Swagger UI
- Requires `DB_PASSWORD` and `JWT_SECRET` to be set (no fallback defaults)
- Suppresses stack traces/messages in error responses

## Troubleshooting

**`ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN` during compile:** your JDK is newer than the Lombok version Spring Boot 3.3.7 defaults to. This project pins `lombok.version` to `1.18.44` in `pom.xml` to fix it — if you still hit this on an even newer JDK, bump that property to the latest Lombok release.

**`No plugin found for prefix 'spring-boot'`:** you're running `mvn` from a folder that doesn't contain this `pom.xml` (often caused by an extra nested folder from unzipping). Run `dir pom.xml` (Windows) or `ls pom.xml` (Mac/Linux) to confirm you're in the right directory first.

## What's new in this update

- **National stage tracking**: `GET /api/stage` (public) returns the current load-shedding stage; `PUT /api/admin/stage` (admin-only) updates it. Shown live on the frontend's home banner.


- **Password reset**: `POST /api/auth/forgot-password` (email), `POST /api/auth/reset-password` (token + new password). Tokens are single-use and expire after 30 minutes.
- **Account lockout**: 5 failed logins locks an account for 15 minutes.
- **Admin console endpoints** (all under `/api/admin`, admin-only): `/stats`, `/users`, `/users/{id}/role`, `/users/{id}/status` (enable/disable), `/users/{id}/password` (force reset), `/users/{id}` (delete). Admins can't demote/disable/delete their own account.
- **Hub ownership**: hubs created by a business owner are linked to that owner (`owner_id`). Owners can edit/delete only their own hubs; admins can manage any hub. New owner-submitted hubs default to unverified until an admin approves them via `PATCH /api/locations/{id}/verify`.
- **`GET /api/locations/mine`**: the signed-in owner's hubs, for a "My hubs" dashboard.
- **Amenity management**: `GET /api/amenities` (public), `POST` / `PUT` / `DELETE /api/amenities/{id}` (admin-only).
- **Admin report moderation**: `GET /api/reports/admin/all` (paged, all check-ins) and admins can now delete any report, not just their own (this was a bug — the ownership check didn't exempt admins).
- **Bug fix**: unhandled `IllegalArgumentException` (e.g. "user not found") used to fall through to a generic 500; it now correctly returns 404 with the real message. Added handlers for access-denied, missing-auth, malformed JSON, and DB constraint-violation cases too.
