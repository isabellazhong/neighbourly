package view.map;

import org.junit.jupiter.api.Test;
import use_case.map.MapImageProvider;
import use_case.map.MapService;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.HeadlessException;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapViewTest {

    static {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void loadsEtasAddressesAndImage() throws Exception {
        MapService mapService = new MapService() {
            @Override
            public RouteInfo getRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) {
                return new RouteInfo(switch (profile) {
                    case "walking" -> 10;
                    case "cycling" -> 6;
                    default -> 3;
                }, "{\"type\":\"LineString\",\"coordinates\":[[0,0],[1,1]]}");
            }

            @Override
            public String reverseGeocode(double lng, double lat) {
                return String.format("Addr %.2f,%.2f", lat, lng);
            }
        };

        MapImageProvider imageProvider = (loc, route) -> new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        RequestLocation location = requestLocation();

        RequestMapView view = new RequestMapView(mapService, imageProvider, "token", location);
        view.waitForLoad();
        SwingUtilities.invokeAndWait(() -> { });

        awaitEquals("Walk: 10 min   Bike: 6 min   Car: 3 min", view::getEtaLabelText);
        awaitTrue(() -> view.getHelperAddressText().contains("Addr"));
        awaitTrue(() -> view.getRequesterAddressText().contains("Addr"));
        awaitTrue(() -> view.getMapIcon() != null);
    }

    @Test
    void handlesLoadFailuresGracefully() throws Exception {
        MapService failingService = new MapService() {
            @Override
            public RouteInfo getRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) {
                throw new RuntimeException("boom");
            }

            @Override
            public String reverseGeocode(double lng, double lat) {
                throw new RuntimeException("addr fail");
            }
        };
        MapImageProvider failingImageProvider = (loc, route) -> { throw new RuntimeException("img"); };

        RequestMapView view = new RequestMapView(
                failingService,
                failingImageProvider,
                "token",
                requestLocation(),
                uri -> { throw new AssertionError("Should not open browser"); },
                (parent, message, title) -> { }
        );

        view.waitForLoad();
        SwingUtilities.invokeAndWait(() -> { });

        awaitEquals("ETA unavailable", view::getEtaLabelText);
        awaitTrue(() -> view.getMapLabelText().contains("Map failed to load"));
    }

    @Test
    void openBrowserWritesHtmlAndUsesLauncher() throws Exception {
        AtomicReference<URI> opened = new AtomicReference<>();
        AtomicBoolean messageShown = new AtomicBoolean(false);

        MapService mapService = new MapService() {
            @Override
            public RouteInfo getRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) {
                return new RouteInfo(5, "{\"type\":\"LineString\",\"coordinates\":[[0,0],[1,1]]}");
            }

            @Override
            public String reverseGeocode(double lng, double lat) {
                return String.format("Address %.1f", lat);
            }
        };
        MapImageProvider provider = (loc, route) -> new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        RequestMapView view = new RequestMapView(
                mapService,
                provider,
                "tok\"en",
                requestLocation(),
                opened::set,
                (parent, message, title) -> messageShown.set(true)
        );

        view.triggerOpenInBrowser();

        assertFalse(messageShown.get());
        assertNotNull(opened.get());
        String html = Files.readString(Path.of(opened.get()));
        assertTrue(html.contains("Helper:"));
        assertTrue(html.contains("tok\\\"en"));
    }

    @Test
    void nullTokenStillBuildsHtml() throws Exception {
        AtomicReference<URI> opened = new AtomicReference<>();
        MapService ms = new MapService() {
            @Override
            public RouteInfo getRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) {
                return new RouteInfo(1, "{\"type\":\"LineString\",\"coordinates\":[[0,0],[1,1]]}");
            }

            @Override
            public String reverseGeocode(double lng, double lat) { return "Addr"; }
        };
        MapImageProvider img = (loc, route) -> new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        RequestMapView view = new RequestMapView(ms, img, null, requestLocation(), opened::set, (p,m,t)->{});
        view.triggerOpenInBrowser();
        assertNotNull(opened.get());
        String html = Files.readString(Path.of(opened.get()));
        assertTrue(html.contains("const token = \"\""));
    }

    @Test
    void waitForLoadHandlesNullWorker() throws Exception {
        MapService ms = new MapService() {
            @Override public RouteInfo getRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) { return new RouteInfo(1,"{}"); }
            @Override public String reverseGeocode(double lng, double lat) { return "A"; }
        };
        MapImageProvider img = (loc, route) -> new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        RequestMapView view = new RequestMapView(ms, img, "t", requestLocation(), uri -> {}, (p,m,t)->{});
        Field f = RequestMapView.class.getDeclaredField("worker");
        f.setAccessible(true);
        f.set(view, null);
        view.waitForLoad(); // should early return without exception
    }

    @Test
    void defaultHelpersAreCallable() {
        assertThrows(Exception.class, () -> RequestMapView.defaultBrowse(URI.create("https://example.com")));
        assertThrows(HeadlessException.class, () -> RequestMapView.defaultShowError(null, "msg", "title"));
    }

    @Test
    void openBrowserReportsErrors() throws Exception {
        AtomicReference<String> error = new AtomicReference<>();

        MapService mapService = new MapService() {
            @Override
            public RouteInfo getRoute(String profile, double fromLng, double fromLat, double toLng, double toLat) {
                return new RouteInfo(5, "{\"type\":\"LineString\",\"coordinates\":[[0,0],[1,1]]}");
            }

            @Override
            public String reverseGeocode(double lng, double lat) {
                return null;
            }
        };
        MapImageProvider provider = (loc, route) -> new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        RequestMapView view = new RequestMapView(
                mapService,
                provider,
                "token",
                requestLocation(),
                uri -> { throw new RuntimeException("boom"); },
                (parent, message, title) -> error.set(message)
        );

        view.triggerOpenInBrowser();

        assertNotNull(error.get());
        assertTrue(error.get().contains("Unable to open interactive map"));
    }

    private static RequestLocation requestLocation() {
        return new RequestLocation(
                "REQ-TEST",
                "Test Request",
                1.0, 2.0,
                3.0, 4.0
        );
    }

    private static void awaitEquals(String expected, Supplier<String> supplier) throws InterruptedException {
        for (int i = 0; i < 80; i++) {
            if (expected.equals(supplier.get())) {
                return;
            }
            Thread.sleep(25);
        }
        fail("Timed out waiting for text: " + expected + " but saw " + supplier.get());
    }

    private static void awaitTrue(Supplier<Boolean> condition) throws InterruptedException {
        for (int i = 0; i < 80; i++) {
            if (condition.get()) {
                return;
            }
            Thread.sleep(25);
        }
        fail("Condition not satisfied in time");
    }
}
