-- Seed: Super Admin account
-- Password: Admin@1234  (BCrypt hash below)
INSERT INTO users (name, email, password_hash, role, email_verified, force_password_change)
VALUES (
    'Super Admin',
    'admin@santediagnostics.com',
    '$2a$12$eImiTXuWVxfM37uY4JANjQ==uqkXjQRIFE8Qm6s8jFsLY5G1yFCym',
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
