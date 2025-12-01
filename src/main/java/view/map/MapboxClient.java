package view.map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.Optional;

/**
 * Minimal Mapbox client for Directions (ETA + route) and Static Map images.
 */
public class MapboxClient {

    public record RouteInfo(double etaMinutes, String geometryJson) {
    }

    private final String token;
    private final HttpClient httpClient;
    private final Gson gson = new Gson();

    public MapboxClient(String token) {
        this.token = token;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Fetch a route and ETA between two points using Mapbox Directions API.
     */
    public RouteInfo fetchRoute(double fromLng, double fromLat, double toLng, double toLat) throws IOException, InterruptedException {
        String url = String.format(Locale.US,
                "https://api.mapbox.com/directions/v5/mapbox/driving/%f,%f;%f,%f?geometries=geojson&overview=full&access_token=%s",
                fromLng, fromLat, toLng, toLat, token
        );
        return fetchRouteWithProfile(url);
    }

    public RouteInfo fetchRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) throws IOException, InterruptedException {
        String url = String.format(Locale.US,
                "https://api.mapbox.com/directions/v5/mapbox/%s/%f,%f;%f,%f?geometries=geojson&overview=full&access_token=%s",
                profile,
                fromLng, fromLat, toLng, toLat, token
        );
        return fetchRouteWithProfile(url);
    }

    private RouteInfo fetchRouteWithProfile(String url) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Directions API error: HTTP " + response.statusCode());
        }

        JsonObject root = gson.fromJson(response.body(), JsonObject.class);
        JsonArray routes = Optional.ofNullable(root.getAsJsonArray("routes"))
                .orElseThrow(() -> new IOException("Directions response missing routes"));

        if (routes.isEmpty()) {
            throw new IOException("No routes returned by Directions API");
        }

        JsonObject route = routes.get(0).getAsJsonObject();
        JsonElement durationEl = route.get("duration");
        JsonElement geometryEl = route.get("geometry");
        if (durationEl == null || geometryEl == null) {
            throw new IOException("Directions response missing duration or geometry");
        }

        double etaMinutes = Math.round(durationEl.getAsDouble() / 60d);
        String geometryJson = gson.toJson(geometryEl);

        return new RouteInfo(etaMinutes, geometryJson);
    }

    /**
     * Reverse geocode a coordinate into a place name.
     */
    public String fetchPlaceName(double lng, double lat) {
        try {
            String url = String.format(Locale.US,
                    "https://api.mapbox.com/geocoding/v5/mapbox.places/%f,%f.json?limit=1&access_token=%s",
                    lng, lat, token);
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return null;
            }
            JsonObject root = gson.fromJson(response.body(), JsonObject.class);
            JsonArray feats = Optional.ofNullable(root.getAsJsonArray("features")).orElse(new JsonArray());
            if (feats.isEmpty()) return null;
            JsonObject first = feats.get(0).getAsJsonObject();
            return first.get("place_name").getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Static map with helper/requester markers and optional route polyline (GeoJSON LineString).
     */
    public BufferedImage fetchStaticMap(RequestLocation location, String routeGeoJson) throws IOException, InterruptedException {
        String overlay;
        if (routeGeoJson != null) {
            String polyline = encodePolyline5(routeGeoJson);
            String path = polyline == null ? "" : "path-5+1d4ed8-0.7(" + polyline + "),";
            overlay = String.format(Locale.US,
                    "%spin-s-h+285A98(%f,%f),pin-s-r+E54A3F(%f,%f)",
                    path,
                    location.helperLng(), location.helperLat(),
                    location.requesterLng(), location.requesterLat()
            );
        } else {
            overlay = String.format(Locale.US,
                    "pin-s-h+285A98(%f,%f),pin-s-r+E54A3F(%f,%f)",
                    location.helperLng(), location.helperLat(),
                    location.requesterLng(), location.requesterLat()
            );
        }

        String url = String.format(Locale.US,
                "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/%s/auto/900x600?padding=80&access_token=%s",
                URLEncoder.encode(overlay, StandardCharsets.UTF_8),
                token
        );

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() != 200) {
            throw new IOException("Static map error: HTTP " + response.statusCode());
        }

        try (ByteArrayInputStream input = new ByteArrayInputStream(response.body())) {
            BufferedImage image = ImageIO.read(input);
            if (image == null) {
                throw new IOException("Failed to decode map image");
            }
            return image;
        }
    }

    private String encodePolyline5(String geoJson) {
        try {
            JsonObject obj = gson.fromJson(geoJson, JsonObject.class);
            if (obj == null || !obj.has("coordinates")) return null;
            JsonArray coords = obj.getAsJsonArray("coordinates");
            StringBuilder sb = new StringBuilder();
            long lastLat = 0, lastLng = 0;
            for (JsonElement el : coords) {
                JsonArray pair = el.getAsJsonArray();
                double lng = pair.get(0).getAsDouble();
                double lat = pair.get(1).getAsDouble();
                long ilat = Math.round(lat * 1e5);
                long ilng = Math.round(lng * 1e5);
                long dlat = ilat - lastLat;
                long dlng = ilng - lastLng;
                encodeSigned(dlat, sb);
                encodeSigned(dlng, sb);
                lastLat = ilat;
                lastLng = ilng;
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void encodeSigned(long v, StringBuilder sb) {
        long s = v << 1;
        if (v < 0) s = ~s;
        while (s >= 0x20) {
            sb.append((char) ((0x20 | (s & 0x1f)) + 63));
            s >>= 5;
        }
        sb.append((char) (s + 63));
    }
}
