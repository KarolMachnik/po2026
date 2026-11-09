package pl.agh.po.diskrak.network;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.function.Predicate;

public class CityGraph {

    public static final BigDecimal STOP_CONSTRUCTION_COST = new BigDecimal("180000.00");
    public static final BigDecimal STOP_WITH_CHARGER_CONSTRUCTION_COST = new BigDecimal("350000.00");
    private static final double MIN_SPLIT_RATIO = 0.05;

    private final Map<UUID, StopNode> nodes = new LinkedHashMap<>();
    private final List<TrackSegment> segments = new ArrayList<>();
    private final Map<UUID, TrackSegment> segmentsById = new HashMap<>();
    private final Map<UUID, List<TrackSegment>> outgoing = new HashMap<>();

    public static CityGraph of(Collection<? extends StopNode> nodes, Collection<TrackSegment> segments) {
        CityGraph graph = new CityGraph();
        nodes.forEach(graph::addNode);
        segments.forEach(graph::addSegment);
        return graph;
    }

    public void addNode(StopNode node) {
        Objects.requireNonNull(node, "node");
        if (nodes.putIfAbsent(node.getId(), node) != null) {
            throw new IllegalArgumentException("Przystanek o id " + node.getId() + " już istnieje w grafie");
        }
        outgoing.put(node.getId(), new ArrayList<>());
    }

    public void addSegment(TrackSegment segment) {
        Objects.requireNonNull(segment, "segment");
        requireRegistered(segment.getFromNode());
        requireRegistered(segment.getToNode());
        if (segmentsById.putIfAbsent(segment.getId(), segment) != null) {
            throw new IllegalArgumentException("Odcinek o id " + segment.getId() + " już istnieje w grafie");
        }
        segments.add(segment);
        outgoing.get(segment.getFromNode().getId()).add(segment);
    }

    public boolean removeSegment(TrackSegment segment) {
        if (segment == null || segmentsById.remove(segment.getId()) == null) {
            return false;
        }
        segments.remove(segment);
        outgoing.get(segment.getFromNode().getId()).remove(segment);
        return true;
    }

    public Optional<StopNode> getNode(UUID id) {
        return Optional.ofNullable(nodes.get(id));
    }

    public Optional<TrackSegment> findSegment(UUID id) {
        return Optional.ofNullable(segmentsById.get(id));
    }

    public List<TrackSegment> getOutgoingSegments(StopNode node) {
        requireRegistered(node);
        return Collections.unmodifiableList(outgoing.get(node.getId()));
    }

    public Map<UUID, StopNode> getNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    public List<TrackSegment> getSegments() {
        return Collections.unmodifiableList(segments);
    }

    public List<TrackSegment> findShortestPath(StopNode start, StopNode end,
                                               Predicate<TrackSegment> canTraversePredicate) {
        requireRegistered(start);
        requireRegistered(end);
        Predicate<TrackSegment> canTraverse = canTraversePredicate == null ? s -> true : canTraversePredicate;
        if (start.equals(end)) {
            return List.of();
        }

        Map<UUID, Double> distances = new HashMap<>();
        Map<UUID, TrackSegment> cameBy = new HashMap<>();
        PriorityQueue<QueueEntry> queue = new PriorityQueue<>();
        distances.put(start.getId(), 0.0);
        queue.add(new QueueEntry(start.getId(), 0.0));

        while (!queue.isEmpty()) {
            QueueEntry current = queue.poll();
            if (current.distance() > distances.getOrDefault(current.nodeId(), Double.POSITIVE_INFINITY)) {
                continue;
            }
            if (current.nodeId().equals(end.getId())) {
                break;
            }
            for (TrackSegment segment : outgoing.get(current.nodeId())) {
                if (segment.isBlocked() || !canTraverse.test(segment)) {
                    continue;
                }
                double weight = segment.getEstimatedTravelMinutes();
                if (Double.isInfinite(weight) || Double.isNaN(weight)) {
                    continue;
                }
                UUID nextId = segment.getToNode().getId();
                double candidate = current.distance() + weight;
                if (candidate < distances.getOrDefault(nextId, Double.POSITIVE_INFINITY)) {
                    distances.put(nextId, candidate);
                    cameBy.put(nextId, segment);
                    queue.add(new QueueEntry(nextId, candidate));
                }
            }
        }

        if (!cameBy.containsKey(end.getId())) {
            return List.of();
        }
        LinkedList<TrackSegment> path = new LinkedList<>();
        UUID cursor = end.getId();
        while (!cursor.equals(start.getId())) {
            TrackSegment segment = cameBy.get(cursor);
            path.addFirst(segment);
            cursor = segment.getFromNode().getId();
        }
        return List.copyOf(path);
    }

    public StopNode buildCustomStopOnSegment(String stopName, Coordinates coords, UUID segmentId,
                                             boolean terminalLoop, boolean charger) {
        Objects.requireNonNull(stopName, "stopName");
        Objects.requireNonNull(coords, "coords");
        TrackSegment original = findSegment(segmentId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono odcinka o id " + segmentId));
        Optional<TrackSegment> reverse = findReverseSegment(original);

        StopNode from = original.getFromNode();
        StopNode to = original.getToNode();
        BigDecimal cost = charger ? STOP_WITH_CHARGER_CONSTRUCTION_COST : STOP_CONSTRUCTION_COST;
        StopNode newStop = new StopNode(UUID.randomUUID(), stopName, from.getDistrictId(), coords,
                terminalLoop, charger, true, cost);

        double ratio = splitRatio(from.getCoordinates(), coords, to.getCoordinates());

        removeSegment(original);
        reverse.ifPresent(this::removeSegment);
        addNode(newStop);

        addSegment(copyOf(original, from, newStop, original.getLengthKm() * ratio));
        addSegment(copyOf(original, newStop, to, original.getLengthKm() * (1.0 - ratio)));

        TrackSegment reverseTemplate = reverse.orElse(original);
        addSegment(copyOf(reverseTemplate, to, newStop, reverseTemplate.getLengthKm() * (1.0 - ratio)));
        addSegment(copyOf(reverseTemplate, newStop, from, reverseTemplate.getLengthKm() * ratio));

        return newStop;
    }

    private Optional<TrackSegment> findReverseSegment(TrackSegment segment) {
        return outgoing.get(segment.getToNode().getId()).stream()
                .filter(candidate -> candidate.getToNode().equals(segment.getFromNode()))
                .filter(candidate -> candidate.getType() == segment.getType())
                .findFirst();
    }

    private static double splitRatio(Coordinates from, Coordinates point, Coordinates to) {
        double toPoint = from.distanceToKm(point);
        double fromPoint = point.distanceToKm(to);
        double total = toPoint + fromPoint;
        double ratio = total <= 0.0 ? 0.5 : toPoint / total;
        return Math.max(MIN_SPLIT_RATIO, Math.min(1.0 - MIN_SPLIT_RATIO, ratio));
    }

    private static TrackSegment copyOf(TrackSegment template, StopNode from, StopNode to, double lengthKm) {
        return new TrackSegment(UUID.randomUUID(), from, to, template.getType(), lengthKm,
                template.getBaseSpeedLimitKmh(), template.getCurrentCongestionFactor(), template.isBlocked(),
                template.isHasPowerSupply());
    }

    private void requireRegistered(StopNode node) {
        Objects.requireNonNull(node, "node");
        if (!nodes.containsKey(node.getId())) {
            throw new IllegalArgumentException("Przystanek " + node.getName() + " nie należy do grafu");
        }
    }

    private record QueueEntry(UUID nodeId, double distance) implements Comparable<QueueEntry> {

        @Override
        public int compareTo(QueueEntry other) {
            return Double.compare(distance, other.distance);
        }
    }
}
