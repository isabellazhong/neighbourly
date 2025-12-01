package interface_adapter.map;

import use_case.map.MapService;
import view.map.MapboxClient;

public class MapboxMapService implements MapService {
    private final MapboxClient client;

    public MapboxMapService(String token) {
        this.client = new MapboxClient(token);
    }

    @Override
    public RouteInfo getRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) throws Exception {
        MapboxClient.RouteInfo r = client.fetchRoute(profile, fromLng, fromLat, toLng, toLat);
        return new RouteInfo(r.etaMinutes(), r.geometryJson());
    }

    @Override
    public String reverseGeocode(double lng, double lat) {
        return client.fetchPlaceName(lng, lat);
    }
}
