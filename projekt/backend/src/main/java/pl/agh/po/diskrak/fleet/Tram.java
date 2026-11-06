package pl.agh.po.diskrak.network;

//ewentualne importy

public class Tram extends Vehicle{
    private boolean hasAutonomousBatteryPack;
    private double batteryRangeRemainingKm;

    @Override public boolean canTraverse(TrackSegment segment){
        if (segment.isBlocked()) return false;
        if (segment.getType() != SegmentType.TRAM_TRACK && segment.getType() !=
                SegmentType.MIXED_ROAD) return false;
// Jeśli w sieci jest prąd, każdy tramwaj pojedzie. Jeśli nie ma prądu, pojedzie tylko Lajkonik z baterią!
        return segment.isHasPowerSupply() || (this.hasAutonomousBatteryPack &&
                this.batteryRangeRemainingKm > segment.getLengthKm());
    }
}