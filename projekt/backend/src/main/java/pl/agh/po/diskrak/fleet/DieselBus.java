package pl.agh.po.diskrak.network;

//ewentualne importy

public  class DieselBus extends Vehicle{
    private double fuelLevelLiters; //np. 250.0
    private String euroStandard; //np. EURO_4

    @Override public boolean canTraverse(TrackSegment segment){
        if (segment.isBlocked()) return false;
        if (segment.getType() != SegmentType.TRAM_TRACK && segment.getType() !=
                SegmentType.MIXED_ROAD) return false;
// Jeśli w sieci jest prąd, każdy tramwaj pojedzie. Jeśli nie ma prądu, pojedzie tylko Lajkonik z baterią!
        return segment.isHasPowerSupply() || (this.hasAutonomousBatteryPack &&
                this.batteryRangeRemainingKm > segment.getLengthKm());
    }
}