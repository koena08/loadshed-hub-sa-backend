# Test data and startup

JPA creates the four-table schema on startup. Register a test citizen through Postman, then use the returned token for report requests:

```json
{"firstName":"Test","lastName":"Citizen","email":"citizen@example.com","password":"Password123"}
```

Passwords must be BCrypt-hashed by `/api/auth/register`.

For local MySQL, create the database once:

```sql
CREATE DATABASE loadshed_hub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
