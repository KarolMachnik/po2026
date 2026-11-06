package pl.agh.po.diskrak.network;

//ewentualne importy

public class ElectricBus extends Vehicle{
    private double batteryCapacityKwh;
    private double currentChargePercent; //tutaj trzeba dać jeszcze range?

    @Override public boolean canTraverse(TrackSegment segment){
        if (segment.isBlocked()) return false;
        if (segment.getType() != SegmentType.TRAM_TRACK && segment.getType() !=
                SegmentType.MIXED_ROAD) return false;
// Jeśli w sieci jest prąd, każdy tramwaj pojedzie. Jeśli nie ma prądu, pojedzie tylko Lajkonik z baterią!
        return segment.isHasPowerSupply() || (this.hasAutonomousBatteryPack &&
                this.batteryRangeRemainingKm > segment.getLengthKm());
    }

    public void chargeAtPantograph(int seconds){
        //zwiększa current charge percent np. o 1,5% co sekundę aż do uzykania 100%
    }
}