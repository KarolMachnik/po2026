package pl.agh.po.diskrak.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pl.agh.po.diskrak.network.RouteType;

import java.util.UUID;

@Entity
@Table(name = "routes")
public class RouteEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "line_number", nullable = false, length = 15)
    private String lineNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "route_type", nullable = false, length = 30)
    private RouteType routeType;

    @Column(name = "frequency_minutes", nullable = false)
    private int frequencyMinutes;

    @Column(name = "is_temporary", nullable = false)
    private boolean temporary;

    @Column(name = "stops_order_json", nullable = false, columnDefinition = "TEXT")
    private String stopsOrderJson;

    @Column(name = "headsign", length = 200)
    private String headsign;

    @Column(name = "operating_hours", length = 50)
    private String operatingHours;

    @Column(name = "operating_days", length = 100)
    private String operatingDays;

    protected RouteEntity() {
    }

    public RouteEntity(UUID id, String lineNumber, RouteType routeType, int frequencyMinutes, boolean temporary,
                       String stopsOrderJson, String headsign, String operatingHours, String operatingDays) {
        this.id = id;
        this.lineNumber = lineNumber;
        this.routeType = routeType;
        this.frequencyMinutes = frequencyMinutes;
        this.temporary = temporary;
        this.stopsOrderJson = stopsOrderJson;
        this.headsign = headsign;
        this.operatingHours = operatingHours;
        this.operatingDays = operatingDays;
    }

    public UUID getId() {
        return id;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public int getFrequencyMinutes() {
        return frequencyMinutes;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public String getStopsOrderJson() {
        return stopsOrderJson;
    }

    public String getHeadsign() {
        return headsign;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public String getOperatingDays() {
        return operatingDays;
    }
}
