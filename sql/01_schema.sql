-- Sante Diagnostics LIMS Schema

CREATE TYPE user_role AS ENUM ('SUPER_ADMIN', 'LAB_ATTENDANT', 'CUSTOMER');
CREATE TYPE payment_status AS ENUM ('UNPAID', 'PAID');
CREATE TYPE sample_status AS ENUM ('COLLECTED', 'PROCESSING', 'VALIDATED', 'DELIVERED');
CREATE TYPE result_format AS ENUM ('NUMERIC', 'TEXT', 'PDF', 'IMAGE');

CREATE TABLE users (
    id                    SERIAL PRIMARY KEY,
    name                  VARCHAR(100) NOT NULL,
    email                 VARCHAR(150) NOT NULL UNIQUE,
    password_hash         VARCHAR(255) NOT NULL,
    role                  user_role NOT NULL,
    email_verified        BOOLEAN NOT NULL DEFAULT FALSE,
    force_password_change BOOLEAN NOT NULL DEFAULT FALSE,
    verification_token    VARCHAR(64),
    created_at            TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE test_types (
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    price            NUMERIC(10,2) NOT NULL,
    turnaround_hours INT NOT NULL,
    result_format    result_format NOT NULL,
    active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE test_requests (
    id             SERIAL PRIMARY KEY,
    customer_id    INT NOT NULL REFERENCES users(id),
    test_type_id   INT NOT NULL REFERENCES test_types(id),
    payment_status payment_status NOT NULL DEFAULT 'UNPAID',
    requested_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    paid_at        TIMESTAMP
);

CREATE TABLE samples (
    id           SERIAL PRIMARY KEY,
    request_id   INT NOT NULL REFERENCES test_requests(id),
    status       sample_status NOT NULL DEFAULT 'COLLECTED',
    collected_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE results (
    id             SERIAL PRIMARY KEY,
    sample_id      INT NOT NULL REFERENCES samples(id),
    text_value     TEXT,
    numeric_value  NUMERIC,
    file_path      VARCHAR(500),
    verified       BOOLEAN NOT NULL DEFAULT FALSE,
    verified_by    INT REFERENCES users(id),
    verified_at    TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Immutable audit log: no updates or deletes ever
CREATE TABLE audit_logs (
    id          SERIAL PRIMARY KEY,
    user_id     INT REFERENCES users(id),
    action      VARCHAR(200) NOT NULL,
    entity_type VARCHAR(50),
    entity_id   INT,
    detail      TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);
