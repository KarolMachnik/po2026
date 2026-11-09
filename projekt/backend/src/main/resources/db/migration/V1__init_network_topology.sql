CREATE TABLE districts (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    population_weight DOUBLE PRECISION NOT NULL CHECK (population_weight > 0),
    base_satisfaction DOUBLE PRECISION NOT NULL DEFAULT 75.0 CHECK (base_satisfaction BETWEEN 0 AND 100),
    punctuality_weight DOUBLE PRECISION NOT NULL CHECK (punctuality_weight >= 0),
    eco_fleet_weight DOUBLE PRECISION NOT NULL CHECK (eco_fleet_weight >= 0),
    overcrowding_weight DOUBLE PRECISION NOT NULL CHECK (overcrowding_weight >= 0),
    replacement_speed_weight DOUBLE PRECISION NOT NULL CHECK (replacement_speed_weight >= 0)
);

CREATE TABLE stop_nodes (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    district_id UUID NOT NULL REFERENCES districts(id),
    lat DOUBLE PRECISION NOT NULL CHECK (lat BETWEEN -90 AND 90),
    lng DOUBLE PRECISION NOT NULL CHECK (lng BETWEEN -180 AND 180),
    is_terminal_loop BOOLEAN NOT NULL DEFAULT FALSE,
    has_pantograph_charger BOOLEAN NOT NULL DEFAULT FALSE,
    is_depot BOOLEAN NOT NULL DEFAULT FALSE,
    depot_capacity INTEGER NOT NULL DEFAULT 0 CHECK (depot_capacity >= 0),
    is_custom_built BOOLEAN NOT NULL DEFAULT FALSE,
    construction_cost NUMERIC(12, 2) NOT NULL DEFAULT 0 CHECK (construction_cost >= 0)
);

CREATE INDEX idx_stop_nodes_district ON stop_nodes(district_id);
CREATE INDEX idx_stop_nodes_coords ON stop_nodes(lat, lng);

CREATE TABLE track_segments (
    id UUID PRIMARY KEY,
    from_node_id UUID NOT NULL REFERENCES stop_nodes(id),
    to_node_id UUID NOT NULL REFERENCES stop_nodes(id),
    segment_type VARCHAR(30) NOT NULL CHECK (segment_type IN ('TRAM_TRACK', 'BUS_LANE', 'MIXED_ROAD')),
    length_km DOUBLE PRECISION NOT NULL CHECK (length_km > 0),
    base_speed_kmh DOUBLE PRECISION NOT NULL CHECK (base_speed_kmh > 0),
    has_power_supply BOOLEAN NOT NULL DEFAULT TRUE,
    congestion_factor DOUBLE PRECISION NOT NULL DEFAULT 1.0 CHECK (congestion_factor >= 1.0),
    is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT chk_track_segments_endpoints CHECK (from_node_id <> to_node_id)
);

CREATE INDEX idx_track_segments_from ON track_segments(from_node_id);
CREATE INDEX idx_track_segments_to ON track_segments(to_node_id);
CREATE INDEX idx_track_segments_type ON track_segments(segment_type);
