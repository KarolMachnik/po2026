CREATE TABLE analytics_hourly_snapshots (
    id BIGSERIAL PRIMARY KEY,
    save_slot_id BIGINT REFERENCES save_slots(id) ON DELETE CASCADE,
    hour_label VARCHAR(20) NOT NULL,
    ticket_revenue NUMERIC(12, 2) NOT NULL,
    city_subsidy NUMERIC(12, 2) NOT NULL,
    energy_cost NUMERIC(12, 2) NOT NULL,
    driver_wages_and_overtime NUMERIC(12, 2) NOT NULL,
    avg_punctuality_percent DOUBLE PRECISION NOT NULL DEFAULT 100.0 CHECK (avg_punctuality_percent BETWEEN 0 AND 100),
    total_passengers_transported INTEGER NOT NULL DEFAULT 0 CHECK (total_passengers_transported >= 0),
    district_scores_json TEXT NOT NULL,
    line_performance_json TEXT NOT NULL,
    driver_workforce_json TEXT NOT NULL
);

CREATE INDEX idx_analytics_slot ON analytics_hourly_snapshots(save_slot_id);

CREATE TABLE dispatch_logs (
    id BIGSERIAL PRIMARY KEY,
    save_slot_id BIGINT REFERENCES save_slots(id) ON DELETE CASCADE,
    logged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    in_game_ticks BIGINT NOT NULL CHECK (in_game_ticks >= 0),
    action_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    cost_pln NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    impact_rating DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    recommendation_id VARCHAR(50)
);

CREATE INDEX idx_dispatch_logs_slot ON dispatch_logs(save_slot_id);
CREATE INDEX idx_dispatch_logs_ticks ON dispatch_logs(in_game_ticks);
