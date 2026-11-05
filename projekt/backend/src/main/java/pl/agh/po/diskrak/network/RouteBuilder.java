package pl.agh.po.diskrak.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class RouteBuilder {

    private final CityGraph cityGraph;
    private final List<StopNode> stops = new ArrayList<>();
    private UUID id;
    private String lineNumber;
    private RouteType routeType;
    private Integer frequencyMinutes;

    public RouteBuilder(CityGraph cityGraph) {
        this.cityGraph = Objects.requireNonNull(cityGraph, "cityGraph");
    }

    public RouteBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public RouteBuilder withLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public RouteBuilder withType(RouteType routeType) {
        this.routeType = routeType;
        return this;
    }

    public RouteBuilder addStop(StopNode stop) {
        stops.add(Objects.requireNonNull(stop, "stop"));
        return this;
    }

    public RouteBuilder withFrequency(int frequencyMinutes) {
        this.frequencyMinutes = frequencyMinutes;
        return this;
    }

    public Route build(Predicate<TrackSegment> canTraversePredicate) {
        if (lineNumber == null || lineNumber.isBlank()) {
            throw new IllegalStateException("Numer linii jest wymagany");
        }
        if (routeType == null) {
            throw new IllegalStateException("Typ linii jest wymagany");
        }
        if (frequencyMinutes == null) {
            throw new IllegalStateException("Częstotliwość kursowania jest wymagana");
        }
        if (stops.size() < 2) {
            throw new IllegalStateException("Linia musi mieć co najmniej dwa przystanki");
        }

        List<TrackSegment> path = new ArrayList<>();
        for (int i = 0; i < stops.size() - 1; i++) {
            StopNode from = stops.get(i);
            StopNode to = stops.get(i + 1);
            if (from.equals(to)) {
                continue;
            }
            List<TrackSegment> leg = cityGraph.findShortestPath(from, to, canTraversePredicate);
            if (leg.isEmpty()) {
                throw new IllegalStateException("Brak przejezdnego połączenia w sieci!");
            }
            path.addAll(leg);
        }

        return new Route(id == null ? UUID.randomUUID() : id, lineNumber, routeType, stops, path,
                frequencyMinutes, routeType == RouteType.TEMPORARY_REPLACEMENT);
    }
}
