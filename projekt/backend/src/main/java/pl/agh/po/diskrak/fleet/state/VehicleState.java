package pl.agh.po.diskrak.network;

// tutaj się da ewentualny import

public interface VehicleState {
    String getStateName(); // np. "IN_TRANSIT", "CHARGING", "BROKEN_DOWN"
    String getBadgeColorHex(); // np. "#22c55e" (zielony), "#ef4444"
(czerwony)
    boolean canMoveInTick(); // zwraca true tylko dla InTransitState oraz
    DelayedState
    boolean isAvailableInDepot(); // zwraca true tylko dla InDepotReserveState
//inne klasy w tym folderze mają implementować ten interfejs
}