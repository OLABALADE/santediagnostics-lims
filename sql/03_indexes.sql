-- Performance indexes
CREATE INDEX idx_users_email          ON users(email);
CREATE INDEX idx_users_role           ON users(role);
CREATE INDEX idx_test_requests_customer  ON test_requests(customer_id);
CREATE INDEX idx_test_requests_payment   ON test_requests(payment_status);
CREATE INDEX idx_samples_request      ON samples(request_id);
CREATE INDEX idx_samples_status       ON samples(status);
CREATE INDEX idx_results_sample       ON results(sample_id);
CREATE INDEX idx_audit_logs_user      ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_created   ON audit_logs(created_at);
