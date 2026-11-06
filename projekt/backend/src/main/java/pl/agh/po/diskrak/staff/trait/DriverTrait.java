package pl.agh.po.diskrak.network;

// tutaj się da ewentualny import

public interface DriverTrait {
    String getTraitName(); // np. "Oaza Spokoju", "Rajdowiec z Alej"
    String getTraitDescription(); // krótki opis na Kartę Kierowcy MPK
    double getEnergyConsumptionMultiplier(); // 1.0 = normalnie, 0.85 = oszczędnie
    double getSpeedMultiplier(); // 1.0 = normalnie, 1.18 = szybciej
    nadrabia opóźnienia
    double getStressGainMultiplier(); // 0.5 = denerwuje się 2x wolniej
    boolean allowsOvertimeWork(); // false tylko dla Związkowca!
}