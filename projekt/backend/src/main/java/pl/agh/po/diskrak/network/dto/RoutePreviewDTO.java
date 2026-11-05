package pl.agh.po.diskrak.network.dto;

import pl.agh.po.diskrak.network.Coordinates;
import pl.agh.po.diskrak.network.RouteType;

import java.util.List;
import java.util.UUID;

public record RoutePreviewDTO(
        UUID routeId,
        String lineNumber,
        RouteType routeType,
        double totalDistanceKm,
        double estimatedTravelMinutes,
        List<Coordinates> polyline,
        List<String> stopNames,
        List<String> bottleneckStopNames
) {

    public RoutePreviewDTO {
        polyline = List.copyOf(polyline);
        stopNames = List.copyOf(stopNames);
        bottleneckStopNames = List.copyOf(bottleneckStopNames);
    }
}
