package pl.agh.po.diskrak.network;

import pl.agh.po.diskrak.network.dto.RoutePreviewDTO;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Route {

    public static final double BOTTLENECK_CONGESTION_THRESHOLD = 1.8;

    private final UUID id;
    private final String lineNumber;
    private final RouteType routeType;
    private final List<StopNode> orderedStops;
    private final List<TrackSegment> pathSegments;
    private final int frequencyMinutes;
    private final boolean isTemporary;

    public Route(UUID id, String lineNumber, RouteType routeType, List<StopNode> orderedStops,
                 List<TrackSegment> pathSegments, int frequencyMinutes, boolean isTemporary) {
        this.id = Objects.requireNonNull(id, "id");
        this.lineNumber = Objects.requireNonNull(lineNumber, "lineNumber");
        this.routeType = Objects.requireNonNull(routeType, "routeType");
        this.orderedStops = List.copyOf(orderedStops);
        this.pathSegments = List.copyOf(pathSegments);
        if (frequencyMinutes <= 0) {
            throw new IllegalArgumentException("Częstotliwość kursowania musi być dodatnia: " + frequencyMinutes);
        }
        this.frequencyMinutes = frequencyMinutes;
        this.isTemporary = isTemporary;
    }

    public RoutePreviewDTO generatePreviewDTO() {
        double totalKm = 0.0;
        double totalMinutes = 0.0;
        Set<String> bottlenecks = new LinkedHashSet<>();
        for (TrackSegment segment : pathSegments) {
            totalKm += segment.getLengthKm();
            totalMinutes += segment.getEstimatedTravelMinutes();
            if (segment.getCurrentCongestionFactor() > BOTTLENECK_CONGESTION_THRESHOLD) {
                bottlenecks.add(segment.getFromNode().getName());
                bottlenecks.add(segment.getToNode().getName());
            }
        }
        List<String> stopNames = orderedStops.stream().map(StopNode::getName).toList();
        return new RoutePreviewDTO(id, lineNumber, routeType, round(totalKm, 2), round(totalMinutes, 1),
                buildPolyline(), stopNames, new ArrayList<>(bottlenecks));
    }

    private List<Coordinates> buildPolyline() {
        List<Coordinates> polyline = new ArrayList<>();
        if (pathSegments.isEmpty()) {
            orderedStops.forEach(stop -> polyline.add(stop.getCoordinates()));
            return polyline;
        }
        polyline.add(pathSegments.get(0).getFromNode().getCoordinates());
        pathSegments.forEach(segment -> polyline.add(segment.getToNode().getCoordinates()));
        return polyline;
    }

    private static double round(double value, int decimals) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return value;
        }
        double scale = Math.pow(10, decimals);
        return Math.round(value * scale) / scale;
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

    public List<StopNode> getOrderedStops() {
        return orderedStops;
    }

    public List<TrackSegment> getPathSegments() {
        return pathSegments;
    }

    public int getFrequencyMinutes() {
        return frequencyMinutes;
    }

    public boolean isTemporary() {
        return isTemporary;
    }
}
