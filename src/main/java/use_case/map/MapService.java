package use_case.map;

public interface MapService {
    RouteInfo getRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) throws Exception;
    String reverseGeocode(double lng, double lat);

    record RouteInfo(double etaMinutes, String geometryJson) {}
}
