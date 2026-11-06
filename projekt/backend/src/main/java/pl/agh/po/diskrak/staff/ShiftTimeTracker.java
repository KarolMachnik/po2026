package pl.agh.po.diskrak.network;

// tutaj się da ewentualny import

public class ShiftTimeTracker {
    private int continousDrivingMinutes = 0; //limit 4h30m
    private int totalShiftMinutesToday = 0; //podstawa 8h + 2 nadgodziny
    private int mandatoryBreakRemainingMinutes = 0;

    public void recordDriving(int minutes) {
        this.continuousDrivingMinutes += minutes;
        this.totalShiftMinutesToday += minutes;
    }
    public void recordBreak(int minutes) {
        this.mandatoryBreakRemainingMinutes = Math.max(0,
                this.mandatoryBreakRemainingMinutes - minutes);
        if (this.mandatoryBreakRemainingMinutes == 0) {
            this.continuousDrivingMinutes = 0; // Po pełnej przerwie licznik jazdy
            ciągłej się zeruje!
        }
    }
    public boolean requiresImmediateBreak() { return continuousDrivingMinutes >= 270; }
    public boolean isApproachingLimit() { return continuousDrivingMinutes >= 250 ||
            totalShiftMinutesToday >= 460; }
    public boolean isInOvertime() { return totalShiftMinutesToday > 480 &&
            totalShiftMinutesToday <= 600; }
    public boolean isHardLimitExceeded() { return totalShiftMinutesToday > 600; }
}