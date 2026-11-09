package pl.agh.po.diskrak.network;

public record Coordinates(double lat, double lng) {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public Coordinates {
        if (lat < -90.0 || lat > 90.0) {
            throw new IllegalArgumentException("Szerokość geograficzna poza zakresem: " + lat);
        }
        if (lng < -180.0 || lng > 180.0) {
            throw new IllegalArgumentException("Długość geograficzna poza zakresem: " + lng);
        }
    }

    public double distanceToKm(Coordinates other) {
        double dLat = Math.toRadians(other.lat - lat);
        double dLng = Math.toRadians(other.lng - lng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(other.lat))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public Coordinates interpolate(Coordinates target, double ratio) {
        double clamped = Math.max(0.0, Math.min(1.0, ratio));
        return new Coordinates(
                lat + (target.lat - lat) * clamped,
                lng + (target.lng - lng) * clamped
        );
    }
}
