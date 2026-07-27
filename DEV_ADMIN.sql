-- Register this account through Postman request 01 first, then run this statement.
-- It preserves the BCrypt password created by the application.
UPDATE users SET role = 'ROLE_ADMIN' WHERE email = 'admin@example.com';
