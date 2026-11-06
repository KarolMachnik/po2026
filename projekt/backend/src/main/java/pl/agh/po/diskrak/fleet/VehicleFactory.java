package pl.agh.po.diskrak.network;

// tutaj się da ewentualny import

public class VehicleFactory {
    public Vehicle purchaseAndCreateVehicle(String modelCode, Depot targetDepot){
        //która za pomocą instrukcji
        //switch (modelCode) tworzy odpowiednio obiekt Tram (dla "LAJKONIK" lub "KRAKOWIAK" ),
        //ElectricBus (dla "SOLARIS_E18" ) albo DieselBus (dla "JELCZ_DIESEL" ), nadaje mu
        //numer boczny (np. "RG" + (900 + counter++) ), pobiera cenę z BudgetManager i parkuje
        //pojazd w targetDepot .
    }
}