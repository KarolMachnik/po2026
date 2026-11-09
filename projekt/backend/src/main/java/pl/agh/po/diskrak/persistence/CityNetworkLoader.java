package pl.agh.po.diskrak.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.po.diskrak.network.CityGraph;
import pl.agh.po.diskrak.network.Coordinates;
import pl.agh.po.diskrak.network.Depot;
import pl.agh.po.diskrak.network.Route;
import pl.agh.po.diskrak.network.SegmentType;
import pl.agh.po.diskrak.network.StopNode;
import pl.agh.po.diskrak.network.TrackSegment;
import pl.agh.po.diskrak.persistence.entity.RouteEntity;
import pl.agh.po.diskrak.persistence.entity.StopNodeEntity;
import pl.agh.po.diskrak.persistence.entity.TrackSegmentEntity;
import pl.agh.po.diskrak.persistence.repository.RouteRepository;
import pl.agh.po.diskrak.persistence.repository.StopNodeRepository;
import pl.agh.po.diskrak.persistence.repository.TrackSegmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Component
public class CityNetworkLoader {

    private static final TypeReference<List<UUID>> UUID_LIST = new TypeReference<>() {
    };

    private final StopNodeRepository stopNodeRepository;
    private final TrackSegmentRepository trackSegmentRepository;
    private final RouteRepository routeRepository;
    private final ObjectMapper objectMapper;

    public CityNetworkLoader(StopNodeRepository stopNodeRepository, TrackSegmentRepository trackSegmentRepository,
                             RouteRepository routeRepository, ObjectMapper objectMapper) {
        this.stopNodeRepository = stopNodeRepository;
        this.trackSegmentRepository = trackSegmentRepository;
        this.routeRepository = routeRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public CityGraph loadGraph() {
        CityGraph graph = new CityGraph();
        stopNodeRepository.findAll().forEach(entity -> graph.addNode(toStopNode(entity)));
        for (TrackSegmentEntity entity : trackSegmentRepository.findAllWithNodes()) {
            StopNode from = graph.getNode(entity.getFromNode().getId()).orElseThrow();
            StopNode to = graph.getNode(entity.getToNode().getId()).orElseThrow();
            graph.addSegment(new TrackSegment(entity.getId(), from, to, entity.getSegmentType(),
                    entity.getLengthKm(), entity.getBaseSpeedKmh(), entity.getCongestionFactor(),
                    entity.isBlocked(), entity.hasPowerSupply()));
        }
        return graph;
    }

    @Transactional(readOnly = true)
    public List<Route> loadRoutes(CityGraph graph) {
        List<Route> routes = new ArrayList<>();
        for (RouteEntity entity : routeRepository.findAll()) {
            List<StopNode> stops = parseStopIds(entity).stream()
                    .map(id -> graph.getNode(id).orElseThrow(() -> new IllegalStateException(
                            "Linia " + entity.getLineNumber() + " odwołuje się do nieznanego przystanku " + id)))
                    .toList();
            routes.add(new Route(entity.getId(), entity.getLineNumber(), entity.getRouteType(), stops,
                    resolvePath(graph, entity, stops), entity.getFrequencyMinutes(), entity.isTemporary()));
        }
        return routes;
    }

    private List<UUID> parseStopIds(RouteEntity entity) {
        try {
            return objectMapper.readValue(entity.getStopsOrderJson(), UUID_LIST);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Niepoprawny stops_order_json dla linii " + entity.getLineNumber(), e);
        }
    }

    private static List<TrackSegment> resolvePath(CityGraph graph, RouteEntity entity, List<StopNode> stops) {
        Predicate<TrackSegment> preferred = prefersTramTrack(entity)
                ? segment -> segment.getType() == SegmentType.TRAM_TRACK
                : segment -> segment.getType() != SegmentType.TRAM_TRACK;
        List<TrackSegment> path = new ArrayList<>();
        for (int i = 0; i < stops.size() - 1; i++) {
            StopNode from = stops.get(i);
            StopNode to = stops.get(i + 1);
            if (from.equals(to)) {
                continue;
            }
            List<TrackSegment> direct = graph.getOutgoingSegments(from).stream()
                    .filter(segment -> segment.getToNode().equals(to))
                    .toList();
            if (!direct.isEmpty()) {
                path.add(direct.stream().filter(preferred).findFirst().orElse(direct.get(0)));
                continue;
            }
            List<TrackSegment> detour = graph.findShortestPath(from, to, segment -> true);
            if (detour.isEmpty()) {
                throw new IllegalStateException("Linia " + entity.getLineNumber() + " nie ma połączenia "
                        + from.getName() + " -> " + to.getName());
            }
            path.addAll(detour);
        }
        return path;
    }

    private static boolean prefersTramTrack(RouteEntity entity) {
        return switch (entity.getRouteType()) {
            case PERMANENT_TRAM -> true;
            case PERMANENT_BUS -> false;
            case TEMPORARY_REPLACEMENT -> entity.getLineNumber().length() < 3;
        };
    }

    private static StopNode toStopNode(StopNodeEntity entity) {
        Coordinates coordinates = new Coordinates(entity.getLat(), entity.getLng());
        UUID districtId = entity.getDistrict().getId();
        if (entity.isDepot()) {
            return new Depot(entity.getId(), entity.getName(), districtId, coordinates,
                    entity.hasPantographCharger(), entity.getDepotCapacity());
        }
        return new StopNode(entity.getId(), entity.getName(), districtId, coordinates, entity.isTerminalLoop(),
                entity.hasPantographCharger(), entity.isCustomBuilt(), entity.getConstructionCost());
    }
}
