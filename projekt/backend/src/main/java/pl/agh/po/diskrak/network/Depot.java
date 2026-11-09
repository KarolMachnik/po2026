package pl.agh.po.diskrak.network;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Depot extends StopNode {

    private final int maxFleetCapacity;
    private final List<UUID> parkedVehicleIds;

    public Depot(UUID id, String name, UUID districtId, Coordinates coordinates, boolean hasPantographCharger,
                 int maxFleetCapacity) {
        super(id, name, districtId, coordinates, true, hasPantographCharger, false, BigDecimal.ZERO);
        if (maxFleetCapacity < 0) {
            throw new IllegalArgumentException("Pojemność zajezdni nie może być ujemna: " + maxFleetCapacity);
        }
        this.maxFleetCapacity = maxFleetCapacity;
        this.parkedVehicleIds = new ArrayList<>();
    }

    public boolean canAcceptVehicle() {
        return parkedVehicleIds.size() < maxFleetCapacity;
    }

    public void parkVehicle(UUID vehicleId) {
        Objects.requireNonNull(vehicleId, "vehicleId");
        if (parkedVehicleIds.contains(vehicleId)) {
            throw new IllegalStateException("Pojazd " + vehicleId + " już stoi w zajezdni " + getName());
        }
        if (!canAcceptVehicle()) {
            throw new IllegalStateException("Zajezdnia " + getName() + " jest pełna (" + maxFleetCapacity + ")");
        }
        parkedVehicleIds.add(vehicleId);
    }

    public void releaseVehicle(UUID vehicleId) {
        if (!parkedVehicleIds.remove(vehicleId)) {
            throw new IllegalStateException("Pojazd " + vehicleId + " nie stoi w zajezdni " + getName());
        }
    }

    public int getMaxFleetCapacity() {
        return maxFleetCapacity;
    }

    public List<UUID> getParkedVehicleIds() {
        return Collections.unmodifiableList(parkedVehicleIds);
    }
}
