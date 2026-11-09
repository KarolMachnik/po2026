CREATE TABLE vehicle_models (
    code VARCHAR(40) PRIMARY KEY,
    model_name VARCHAR(100) NOT NULL,
    vehicle_category VARCHAR(30) NOT NULL CHECK (vehicle_category IN ('TRAM', 'ELECTRIC_BUS', 'DIESEL_BUS')),
    max_capacity INTEGER NOT NULL CHECK (max_capacity > 0),
    purchase_price NUMERIC(12, 2) NOT NULL CHECK (purchase_price > 0),
    cost_per_km NUMERIC(8, 2) NOT NULL CHECK (cost_per_km >= 0),
    eco_score DOUBLE PRECISION NOT NULL CHECK (eco_score BETWEEN 0 AND 10),
    has_autonomous_battery BOOLEAN NOT NULL DEFAULT FALSE,
    battery_capacity_kwh DOUBLE PRECISION NOT NULL DEFAULT 0 CHECK (battery_capacity_kwh >= 0),
    energy_consumption_per_km DOUBLE PRECISION NOT NULL DEFAULT 0 CHECK (energy_consumption_per_km >= 0),
    fuel_tank_capacity_liters DOUBLE PRECISION NOT NULL DEFAULT 0 CHECK (fuel_tank_capacity_liters >= 0)
);

CREATE TABLE routes (
    id UUID PRIMARY KEY,
    line_number VARCHAR(15) NOT NULL,
    route_type VARCHAR(30) NOT NULL CHECK (route_type IN ('PERMANENT_TRAM', 'PERMANENT_BUS', 'TEMPORARY_REPLACEMENT')),
    frequency_minutes INTEGER NOT NULL CHECK (frequency_minutes > 0),
    is_temporary BOOLEAN NOT NULL DEFAULT FALSE,
    stops_order_json TEXT NOT NULL,
    headsign VARCHAR(200),
    operating_hours VARCHAR(50),
    operating_days VARCHAR(100)
);

CREATE INDEX idx_routes_line_number ON routes(line_number);

CREATE TABLE route_stops (
    id BIGSERIAL PRIMARY KEY,
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    stop_node_id UUID NOT NULL REFERENCES stop_nodes(id),
    stop_sequence_order INTEGER NOT NULL CHECK (stop_sequence_order >= 1),
    travel_time_from_prev_minutes INTEGER NOT NULL DEFAULT 2 CHECK (travel_time_from_prev_minutes >= 0),
    CONSTRAINT uq_route_stops_sequence UNIQUE (route_id, stop_sequence_order)
);

CREATE INDEX idx_route_stops_route ON route_stops(route_id);
CREATE INDEX idx_route_stops_node ON route_stops(stop_node_id);
