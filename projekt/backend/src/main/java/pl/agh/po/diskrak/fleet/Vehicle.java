package pl.agh.po.diskrak.network;

//ewentualne importy
import java.util.UUID;
import java.math.BigDecimal;

public abstract class Vehicle implements EnergyConsumer{
    protected UUID id;
    protected String sideNumber;
    protected String modelName;
    protected int maxCapacity;
    protected int currentPassengers;
    protected BigDecimal purchasePrice;
    protected BigDecimal costPerKm;
    protected double ecoScore;
    protected Coordinates currentPosition;
    protected VehicleState currentState;
    protected UUID assignedRouteId;
    protected UUID assignedDriverId;

    public abstract boolean canTraverse(TrackSegment segment);
    public abstract void consumeEnergy(double distanceKm, double congestionFactor);

    public void changeState(VehicleState newState){
        //ustawia pole this.currentState = newState
    }
    public int boardPassengers(int waitingAtStop){
        //Co jakiś czas nowy pasażer przychodzi na przystanek
    }
    public void alightPassengers(double fraction){
        //Na każdym przystanku jakiś procent autobusu (dane z bazy) wysiada
    }
}