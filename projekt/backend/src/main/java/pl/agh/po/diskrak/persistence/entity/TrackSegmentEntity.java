package pl.agh.po.diskrak.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pl.agh.po.diskrak.network.SegmentType;

import java.util.UUID;

@Entity
@Table(name = "track_segments")
public class TrackSegmentEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_node_id", nullable = false)
    private StopNodeEntity fromNode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_node_id", nullable = false)
    private StopNodeEntity toNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "segment_type", nullable = false, length = 30)
    private SegmentType segmentType;

    @Column(name = "length_km", nullable = false)
    private double lengthKm;

    @Column(name = "base_speed_kmh", nullable = false)
    private double baseSpeedKmh;

    @Column(name = "has_power_supply", nullable = false)
    private boolean powerSupply;

    @Column(name = "congestion_factor", nullable = false)
    private double congestionFactor;

    @Column(name = "is_blocked", nullable = false)
    private boolean blocked;

    protected TrackSegmentEntity() {
    }

    public TrackSegmentEntity(UUID id, StopNodeEntity fromNode, StopNodeEntity toNode, SegmentType segmentType,
                              double lengthKm, double baseSpeedKmh, boolean powerSupply, double congestionFactor,
                              boolean blocked) {
        this.id = id;
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.segmentType = segmentType;
        this.lengthKm = lengthKm;
        this.baseSpeedKmh = baseSpeedKmh;
        this.powerSupply = powerSupply;
        this.congestionFactor = congestionFactor;
        this.blocked = blocked;
    }

    public UUID getId() {
        return id;
    }

    public StopNodeEntity getFromNode() {
        return fromNode;
    }

    public StopNodeEntity getToNode() {
        return toNode;
    }

    public SegmentType getSegmentType() {
        return segmentType;
    }

    public double getLengthKm() {
        return lengthKm;
    }

    public double getBaseSpeedKmh() {
        return baseSpeedKmh;
    }

    public boolean hasPowerSupply() {
        return powerSupply;
    }

    public double getCongestionFactor() {
        return congestionFactor;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
