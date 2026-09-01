-- Runs only on first Postgres volume init (docker entrypoint).
-- loan_management_db is already created via POSTGRES_DB env.
-- user-auth-service expects user_db; notification-service expects notification_db.

CREATE DATABASE user_db;
CREATE DATABASE notification_db;
