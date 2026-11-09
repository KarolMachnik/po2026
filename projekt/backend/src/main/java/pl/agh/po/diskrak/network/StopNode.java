package pl.agh.po.diskrak.network;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class StopNode {

    private final UUID id;
    private final String name;
    private final UUID districtId;
    private final Coordinates coordinates;
    private int waitingPassengers;
    private final boolean isTerminalLoop;
    private final boolean hasPantographCharger;
    private final boolean isCustomBuilt;
    private final BigDecimal constructionCost;

    public StopNode(UUID id, String name, UUID districtId, Coordinates coordinates, boolean isTerminalLoop,
                    boolean hasPantographCharger, boolean isCustomBuilt, BigDecimal constructionCost) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.districtId = districtId;
        this.coordinates = Objects.requireNonNull(coordinates, "coordinates");
        this.isTerminalLoop = isTerminalLoop;
        this.hasPantographCharger = hasPantographCharger;
        this.isCustomBuilt = isCustomBuilt;
        this.constructionCost = constructionCost == null ? BigDecimal.ZERO : constructionCost;
        this.waitingPassengers = 0;
    }

    public void addPassengers(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Liczba pasażerów nie może być ujemna: " + count);
        }
        waitingPassengers += count;
    }

    public int boardPassengers(int availableSeats) {
        int boarding = Math.min(waitingPassengers, Math.max(0, availableSeats));
        waitingPassengers -= boarding;
        return boarding;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getDistrictId() {
        return districtId;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getWaitingPassengers() {
        return waitingPassengers;
    }

    public boolean isTerminalLoop() {
        return isTerminalLoop;
    }

    public boolean hasPantographCharger() {
        return hasPantographCharger;
    }

    public boolean isCustomBuilt() {
        return isCustomBuilt;
    }

    public BigDecimal getConstructionCost() {
        return constructionCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StopNode other)) {
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
        return getClass().getSimpleName() + "{" + name + ", " + id + "}";
    }
}
