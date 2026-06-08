-- Password: Admin@1234
INSERT INTO users (name, email, password_hash, role, email_verified, force_password_change)
VALUES (
    'Super Admin',
    'admin@santediagnostics.com',
    '$2a$12$g4cODqX7pBZsA8j/glFkQu/Ve0ur8Qz/EA7/Y1Fl65YHlUUhgJUwq',
    'SUPER_ADMIN',
    TRUE,
    FALSE
);

-- Sample test types
INSERT INTO test_types (name, price, turnaround_hours, result_format) VALUES
    ('Full Blood Count',      1500.00, 24,  'NUMERIC'),
    ('Malaria RDT',            800.00,  4,  'TEXT'),
    ('Hepatitis B Surface Ag',1200.00, 48,  'TEXT'),
    ('Chest X-Ray',           2500.00, 12,  'IMAGE'),
    ('COVID-19 PCR',          5000.00, 48,  'PDF');