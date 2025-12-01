package interface_adapter.map;

import use_case.map.MapImageProvider;
import view.map.MapboxClient;
import view.map.RequestLocation;

import java.awt.image.BufferedImage;

public class RealMapImageProvider implements MapImageProvider {
    private final MapboxClient client;

    public RealMapImageProvider(String token) {
        this(new MapboxClient(token));
    }

    RealMapImageProvider(MapboxClient client) {
        this.client = client;
    }

    @Override
    public BufferedImage loadImage(RequestLocation location, String routeGeoJson) throws Exception {
        return client.fetchStaticMap(location, routeGeoJson);
    }
}
