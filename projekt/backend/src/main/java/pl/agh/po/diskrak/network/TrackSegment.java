package pl.agh.po.diskrak.network;

import java.util.Objects;
import java.util.UUID;

public class TrackSegment {

    private final UUID id;
    private final StopNode fromNode;
    private final StopNode toNode;
    private final SegmentType type;
    private final double lengthKm;
    private final double baseSpeedLimitKmh;
    private double currentCongestionFactor;
    private boolean isBlocked;
    private boolean hasPowerSupply;

    public TrackSegment(UUID id, StopNode fromNode, StopNode toNode, SegmentType type, double lengthKm,
                        double baseSpeedLimitKmh) {
        this(id, fromNode, toNode, type, lengthKm, baseSpeedLimitKmh, 1.0, false, true);
    }

    public TrackSegment(UUID id, StopNode fromNode, StopNode toNode, SegmentType type, double lengthKm,
                        double baseSpeedLimitKmh, double currentCongestionFactor, boolean isBlocked,
                        boolean hasPowerSupply) {
        this.id = Objects.requireNonNull(id, "id");
        this.fromNode = Objects.requireNonNull(fromNode, "fromNode");
        this.toNode = Objects.requireNonNull(toNode, "toNode");
        this.type = Objects.requireNonNull(type, "type");
        if (fromNode.equals(toNode)) {
            throw new IllegalArgumentException("Odcinek musi łączyć dwa różne przystanki: " + fromNode.getName());
        }
        if (lengthKm <= 0) {
            throw new IllegalArgumentException("Długość odcinka musi być dodatnia: " + lengthKm);
        }
        if (baseSpeedLimitKmh <= 0) {
            throw new IllegalArgumentException("Prędkość bazowa musi być dodatnia: " + baseSpeedLimitKmh);
        }
        this.lengthKm = lengthKm;
        this.baseSpeedLimitKmh = baseSpeedLimitKmh;
        this.currentCongestionFactor = currentCongestionFactor;
        this.isBlocked = isBlocked;
        this.hasPowerSupply = hasPowerSupply;
    }

    public double getEffectiveSpeedKmh() {
        return isBlocked ? 0.0 : baseSpeedLimitKmh / Math.max(1.0, currentCongestionFactor);
    }

    public double getEstimatedTravelMinutes() {
        double speed = getEffectiveSpeedKmh();
        if (speed <= 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        return (lengthKm / speed) * 60.0;
    }

    public UUID getId() {
        return id;
    }

    public StopNode getFromNode() {
        return fromNode;
    }

    public StopNode getToNode() {
        return toNode;
    }

    public SegmentType getType() {
        return type;
    }

    public double getLengthKm() {
        return lengthKm;
    }

    public double getBaseSpeedLimitKmh() {
        return baseSpeedLimitKmh;
    }

    public double getCurrentCongestionFactor() {
        return currentCongestionFactor;
    }

    public void setCurrentCongestionFactor(double currentCongestionFactor) {
        this.currentCongestionFactor = currentCongestionFactor;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }

    public boolean isHasPowerSupply() {
        return hasPowerSupply;
    }

    public void setHasPowerSupply(boolean hasPowerSupply) {
        this.hasPowerSupply = hasPowerSupply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrackSegment other)) {
            return false;
        }
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "TrackSegment{" + fromNode.getName() + " -> " + toNode.getName() + ", " + type
                + ", " + lengthKm + " km}";
    }
}
