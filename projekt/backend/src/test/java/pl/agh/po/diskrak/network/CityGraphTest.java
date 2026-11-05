package pl.agh.po.diskrak.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.agh.po.diskrak.network.dto.RoutePreviewDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityGraphTest {

    private static final UUID DISTRICT = UUID.randomUUID();

    private CityGraph graph;
    private StopNode bagatela;
    private StopNode basztowa;
    private StopNode dworzec;
    private StopNode mogilskie;
    private TrackSegment bagatelaBasztowa;
    private TrackSegment basztowaDworzec;
    private TrackSegment bagatelaMogilskieBus;
    private TrackSegment dworzecMogilskie;

    @BeforeEach
    void setUp() {
        graph = new CityGraph();
        bagatela = stop("Teatr Bagatela", 50.0635, 19.9330);
        basztowa = stop("Basztowa LOT", 50.0659, 19.9422);
        dworzec = stop("Dworzec Główny Tunel", 50.0682, 19.9477);
        mogilskie = stop("Rondo Mogilskie", 50.0659, 19.9592);
        List.of(bagatela, basztowa, dworzec, mogilskie).forEach(graph::addNode);

        bagatelaBasztowa = bothWays(bagatela, basztowa, SegmentType.TRAM_TRACK, 0.8, 30);
        basztowaDworzec = bothWays(basztowa, dworzec, SegmentType.TRAM_TRACK, 0.6, 30);
        dworzecMogilskie = bothWays(dworzec, mogilskie, SegmentType.TRAM_TRACK, 0.9, 30);
        bagatelaMogilskieBus = bothWays(bagatela, mogilskie, SegmentType.MIXED_ROAD, 2.6, 25);
    }

    @Test
    void haversineGivesRealisticDistance() {
        Coordinates rynek = new Coordinates(50.0617, 19.9373);
        Coordinates wawel = new Coordinates(50.0540, 19.9354);
        assertEquals(0.87, rynek.distanceToKm(wawel), 0.03);
        assertEquals(0.0, rynek.distanceToKm(rynek), 1e-9);
    }

    @Test
    void interpolateMovesAlongLineAndClampsRatio() {
        Coordinates a = new Coordinates(50.0, 19.0);
        Coordinates b = new Coordinates(51.0, 20.0);
        assertEquals(new Coordinates(50.5, 19.5), a.interpolate(b, 0.5));
        assertEquals(a, a.interpolate(b, -1));
        assertEquals(b, a.interpolate(b, 2));
    }

    @Test
    void boardPassengersTakesAtMostAvailableSeats() {
        bagatela.addPassengers(40);
        assertEquals(25, bagatela.boardPassengers(25));
        assertEquals(15, bagatela.getWaitingPassengers());
        assertEquals(15, bagatela.boardPassengers(100));
        assertEquals(0, bagatela.getWaitingPassengers());
        assertThrows(IllegalArgumentException.class, () -> bagatela.addPassengers(-1));
    }

    @Test
    void depotRespectsCapacity() {
        Depot depot = new Depot(UUID.randomUUID(), "Zajezdnia Podgórze", DISTRICT,
                new Coordinates(50.0215, 19.9352), false, 1);
        UUID tram = UUID.randomUUID();
        assertTrue(depot.canAcceptVehicle());
        depot.parkVehicle(tram);
        assertFalse(depot.canAcceptVehicle());
        assertThrows(IllegalStateException.class, () -> depot.parkVehicle(UUID.randomUUID()));
        depot.releaseVehicle(tram);
        assertTrue(depot.canAcceptVehicle());
        assertThrows(IllegalStateException.class, () -> depot.releaseVehicle(tram));
    }

    @Test
    void effectiveSpeedAndTravelTimeFollowCongestionAndBlockade() {
        TrackSegment segment = new TrackSegment(UUID.randomUUID(), bagatela, basztowa, SegmentType.BUS_LANE, 1.0, 30);
        assertEquals(2.0, segment.getEstimatedTravelMinutes(), 1e-9);
        segment.setCurrentCongestionFactor(2.0);
        assertEquals(15.0, segment.getEffectiveSpeedKmh(), 1e-9);
        assertEquals(4.0, segment.getEstimatedTravelMinutes(), 1e-9);
        segment.setCurrentCongestionFactor(0.5);
        assertEquals(30.0, segment.getEffectiveSpeedKmh(), 1e-9);
        segment.setBlocked(true);
        assertEquals(0.0, segment.getEffectiveSpeedKmh());
        assertEquals(Double.POSITIVE_INFINITY, segment.getEstimatedTravelMinutes());
    }

    @Test
    void dijkstraPicksFastestPath() {
        List<TrackSegment> path = graph.findShortestPath(bagatela, mogilskie, s -> true);
        assertEquals(List.of(bagatelaBasztowa, basztowaDworzec, dworzecMogilskie), path);
    }

    @Test
    void dijkstraRespectsPredicateForVehicleType() {
        List<TrackSegment> busPath = graph.findShortestPath(bagatela, mogilskie,
                s -> s.getType() != SegmentType.TRAM_TRACK);
        assertEquals(List.of(bagatelaMogilskieBus), busPath);

        List<TrackSegment> poweredOnly = graph.findShortestPath(bagatela, mogilskie, TrackSegment::isHasPowerSupply);
        assertEquals(3, poweredOnly.size());
    }

    @Test
    void dijkstraAvoidsBlockedAndUnpoweredSegments() {
        basztowaDworzec.setBlocked(true);
        assertEquals(List.of(bagatelaMogilskieBus), graph.findShortestPath(bagatela, mogilskie, s -> true));

        basztowaDworzec.setBlocked(false);
        basztowaDworzec.setHasPowerSupply(false);
        assertTrue(graph.findShortestPath(bagatela, mogilskie,
                s -> s.getType() == SegmentType.TRAM_TRACK && s.isHasPowerSupply()).isEmpty());
    }

    @Test
    void dijkstraReturnsEmptyForSameNodeAndRejectsForeignNode() {
        assertTrue(graph.findShortestPath(bagatela, bagatela, s -> true).isEmpty());
        StopNode foreign = stop("Poza grafem", 50.1, 19.9);
        assertThrows(IllegalArgumentException.class, () -> graph.findShortestPath(bagatela, foreign, s -> true));
    }

    @Test
    void buildCustomStopSplitsSegmentInBothDirections() {
        int segmentsBefore = graph.getSegments().size();
        Coordinates midpoint = basztowa.getCoordinates().interpolate(dworzec.getCoordinates(), 0.25);

        StopNode built = graph.buildCustomStopOnSegment("Nowy Przystanek", midpoint, basztowaDworzec.getId(),
                false, true);

        assertTrue(built.isCustomBuilt());
        assertTrue(built.hasPantographCharger());
        assertEquals(CityGraph.STOP_WITH_CHARGER_CONSTRUCTION_COST, built.getConstructionCost());
        assertEquals(built, graph.getNode(built.getId()).orElseThrow());
        assertTrue(graph.findSegment(basztowaDworzec.getId()).isEmpty());
        assertEquals(segmentsBefore + 2, graph.getSegments().size());

        List<TrackSegment> forward = graph.findShortestPath(basztowa, dworzec, s -> true);
        assertEquals(2, forward.size());
        assertEquals(built, forward.get(0).getToNode());
        assertEquals(0.6, forward.get(0).getLengthKm() + forward.get(1).getLengthKm(), 1e-9);
        assertTrue(forward.get(0).getLengthKm() < forward.get(1).getLengthKm());
        assertEquals(SegmentType.TRAM_TRACK, forward.get(0).getType());

        List<TrackSegment> backward = graph.findShortestPath(dworzec, basztowa, s -> true);
        assertEquals(2, backward.size());
        assertEquals(built, backward.get(0).getToNode());
    }

    @Test
    void buildCustomStopWithoutChargerCostsLess() {
        StopNode built = graph.buildCustomStopOnSegment("Tani Przystanek", basztowa.getCoordinates(),
                bagatelaBasztowa.getId(), true, false);
        assertEquals(CityGraph.STOP_CONSTRUCTION_COST, built.getConstructionCost());
        assertEquals(new BigDecimal("180000.00"), built.getConstructionCost());
        assertTrue(built.isTerminalLoop());
        assertThrows(IllegalArgumentException.class,
                () -> graph.buildCustomStopOnSegment("X", basztowa.getCoordinates(), UUID.randomUUID(), false, false));
    }

    @Test
    void routeBuilderJoinsLegsAndGeneratesPreview() {
        basztowaDworzec.setCurrentCongestionFactor(2.5);

        Route route = new RouteBuilder(graph)
                .withLineNumber("704")
                .withType(RouteType.TEMPORARY_REPLACEMENT)
                .addStop(bagatela)
                .addStop(dworzec)
                .addStop(mogilskie)
                .withFrequency(6)
                .build(s -> true);

        assertTrue(route.isTemporary());
        assertEquals(6, route.getFrequencyMinutes());
        assertEquals(List.of(bagatelaBasztowa, basztowaDworzec, dworzecMogilskie), route.getPathSegments());

        RoutePreviewDTO preview = route.generatePreviewDTO();
        assertEquals("704", preview.lineNumber());
        assertEquals(2.3, preview.totalDistanceKm(), 1e-9);
        assertEquals(1.6 + 3.0 + 1.8, preview.estimatedTravelMinutes(), 0.05);
        assertEquals(List.of("Teatr Bagatela", "Dworzec Główny Tunel", "Rondo Mogilskie"), preview.stopNames());
        assertEquals(List.of("Basztowa LOT", "Dworzec Główny Tunel"), preview.bottleneckStopNames());
        assertEquals(4, preview.polyline().size());
        assertEquals(bagatela.getCoordinates(), preview.polyline().get(0));
        assertEquals(mogilskie.getCoordinates(), preview.polyline().get(3));
    }

    @Test
    void routeBuilderThrowsWhenNoTraversablePath() {
        RouteBuilder builder = new RouteBuilder(graph)
                .withLineNumber("4")
                .withType(RouteType.PERMANENT_TRAM)
                .addStop(bagatela)
                .addStop(mogilskie)
                .withFrequency(10);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> builder.build(s -> false));
        assertEquals("Brak przejezdnego połączenia w sieci!", ex.getMessage());
    }

    @Test
    void routeBuilderValidatesRequiredFields() {
        assertThrows(IllegalStateException.class, () -> new RouteBuilder(graph)
                .withType(RouteType.PERMANENT_BUS).addStop(bagatela).addStop(mogilskie).withFrequency(5)
                .build(s -> true));
        assertThrows(IllegalStateException.class, () -> new RouteBuilder(graph)
                .withLineNumber("179").withType(RouteType.PERMANENT_BUS).addStop(bagatela).withFrequency(5)
                .build(s -> true));
    }

    private static StopNode stop(String name, double lat, double lng) {
        return new StopNode(UUID.randomUUID(), name, DISTRICT, new Coordinates(lat, lng), false, false, false,
                BigDecimal.ZERO);
    }

    private TrackSegment bothWays(StopNode a, StopNode b, SegmentType type, double km, double speed) {
        boolean powered = type == SegmentType.TRAM_TRACK;
        TrackSegment forward = new TrackSegment(UUID.randomUUID(), a, b, type, km, speed, 1.0, false, powered);
        graph.addSegment(forward);
        graph.addSegment(new TrackSegment(UUID.randomUUID(), b, a, type, km, speed, 1.0, false, powered));
        return forward;
    }
}
