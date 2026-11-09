CREATE TABLE save_slots (
    id BIGSERIAL PRIMARY KEY,
    save_name VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    in_game_ticks BIGINT NOT NULL CHECK (in_game_ticks >= 0),
    budget_pln NUMERIC(14, 2) NOT NULL,
    dispatcher_score DOUBLE PRECISION NOT NULL CHECK (dispatcher_score BETWEEN 0 AND 100),
    dispatcher_rank VARCHAR(50) NOT NULL,
    world_state_json TEXT NOT NULL
);

CREATE INDEX idx_save_slots_created_at ON save_slots(created_at);

CREATE TABLE saved_drivers (
    id BIGSERIAL PRIMARY KEY,
    save_slot_id BIGINT NOT NULL REFERENCES save_slots(id) ON DELETE CASCADE,
    employee_code VARCHAR(30) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    age INTEGER NOT NULL CHECK (age BETWEEN 18 AND 75),
    avatar_seed VARCHAR(50),
    hourly_wage_pln NUMERIC(8, 2) NOT NULL CHECK (hourly_wage_pln > 0),
    licenses_json JSONB NOT NULL,
    trait_type VARCHAR(50) NOT NULL,
    continuous_driving_minutes INTEGER NOT NULL DEFAULT 0 CHECK (continuous_driving_minutes >= 0),
    mandatory_break_remaining_minutes INTEGER NOT NULL DEFAULT 0 CHECK (mandatory_break_remaining_minutes >= 0),
    total_shift_minutes_today INTEGER NOT NULL DEFAULT 0 CHECK (total_shift_minutes_today >= 0),
    fatigue_level NUMERIC(5, 2) NOT NULL DEFAULT 0.00 CHECK (fatigue_level BETWEEN 0.00 AND 100.00),
    stress_level NUMERIC(5, 2) NOT NULL DEFAULT 0.00 CHECK (stress_level BETWEEN 0.00 AND 100.00),
    current_state VARCHAR(30) NOT NULL CHECK (current_state IN ('RESTING', 'ON_DUTY_DRIVING', 'ON_MANDATORY_BREAK', 'OVERTIME', 'REFUSED_TO_WORK')),
    CONSTRAINT uq_saved_drivers_code UNIQUE (save_slot_id, employee_code)
);

CREATE INDEX idx_saved_drivers_slot ON saved_drivers(save_slot_id);
CREATE INDEX idx_saved_drivers_state ON saved_drivers(current_state);

CREATE TABLE saved_vehicles (
    id BIGSERIAL PRIMARY KEY,
    save_slot_id BIGINT NOT NULL REFERENCES save_slots(id) ON DELETE CASCADE,
    model_code VARCHAR(40) NOT NULL REFERENCES vehicle_models(code),
    vehicle_category VARCHAR(30) NOT NULL CHECK (vehicle_category IN ('TRAM', 'ELECTRIC_BUS', 'DIESEL_BUS')),
    side_number VARCHAR(30) NOT NULL,
    current_passengers INTEGER NOT NULL DEFAULT 0 CHECK (current_passengers >= 0),
    energy_or_fuel_level NUMERIC(6, 2) NOT NULL CHECK (energy_or_fuel_level >= 0),
    current_state VARCHAR(30) NOT NULL CHECK (current_state IN ('IN_TRANSIT', 'WAITING_FOR_DRIVER_RELIEF', 'DRIVER_ON_BREAK', 'CHARGING', 'BROKEN_DOWN', 'IN_DEPOT_RESERVE')),
    assigned_driver_id BIGINT REFERENCES saved_drivers(id) ON DELETE SET NULL,
    assigned_route_id UUID REFERENCES routes(id) ON DELETE SET NULL,
    current_lat DOUBLE PRECISION NOT NULL,
    current_lng DOUBLE PRECISION NOT NULL,
    CONSTRAINT uq_saved_vehicles_side_number UNIQUE (save_slot_id, side_number)
);

CREATE INDEX idx_saved_vehicles_slot ON saved_vehicles(save_slot_id);
CREATE INDEX idx_saved_vehicles_driver ON saved_vehicles(assigned_driver_id);
CREATE INDEX idx_saved_vehicles_route ON saved_vehicles(assigned_route_id);

CREATE TABLE saved_active_incidents (
    id BIGSERIAL PRIMARY KEY,
    save_slot_id BIGINT NOT NULL REFERENCES save_slots(id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    severity VARCHAR(30) NOT NULL CHECK (severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    remaining_ticks INTEGER NOT NULL CHECK (remaining_ticks >= 0),
    affected_segments_json JSONB NOT NULL,
    effects_json JSONB NOT NULL
);

CREATE INDEX idx_saved_incidents_slot ON saved_active_incidents(save_slot_id);
