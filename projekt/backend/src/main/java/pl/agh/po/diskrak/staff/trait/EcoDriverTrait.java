package pl.agh.po.diskrak.network;

// tutaj się da ewentualny import

public class EcoDriverTrait implements DriverTrait {

    @Override
    public String getTraitName() {
        return "Mistrz Eko-Jazdy";
    }

    @Override
    public String getTraitDescription() {
        return "Zawsze dba o płynne ruszanie i hamowanie."; // Tu możesz wpisać dowolny krótki opis
    }

    @Override
    public double getEnergyConsumptionMultiplier() {
        return 0.85;
    }

    @Override
    public double getSpeedMultiplier() {
        return 1.0;
    }

    @Override
    public double getStressGainMultiplier() {
        return 1.0;
    }

    @Override
    public boolean allowsOvertimeWork() {
        return true;
    }
}