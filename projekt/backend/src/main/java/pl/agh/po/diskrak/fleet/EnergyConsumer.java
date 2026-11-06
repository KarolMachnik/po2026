package pl.agh.po.diskrak.network;

// tutaj się da ewentualny import

public interface EnergyConsumer {
    public double calculateEnergyConsumption(double distanceKm, double congestionFactor);
    public double getEnergyLevelPercent();
    public boolean canCompleteRoute(Route route);
}