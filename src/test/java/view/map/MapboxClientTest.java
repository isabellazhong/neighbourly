package view.map;

import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class MapboxClientTest {

    @Test
    void fetchRouteDrivingSuccess() throws Exception {
        MapboxClient client = clientResponding(req -> response(200, directionsJson(180)));
        MapboxClient.RouteInfo info = client.fetchRoute(0, 0, 1, 1);

        assertEquals(3, info.etaMinutes());
        assertEquals("{\"type\":\"LineString\",\"coordinates\":[[0,0],[1,1]]}", info.geometryJson());
    }

    @Test
    void fetchRouteProfileSuccess() throws Exception {
        MapboxClient client = clientResponding(req -> response(200, directionsJson(60)));
        MapboxClient.RouteInfo info = client.fetchRoute("walking", 0, 0, 0, 0);
        assertEquals(1, info.etaMinutes());
    }

    @Test
    void fetchRouteErrorsOnStatus() {
        MapboxClient client = clientResponding(req -> response(500, "error"));
        assertThrows(IOException.class, () -> client.fetchRoute(0, 0, 0, 0));
    }

    @Test
    void fetchRouteErrorsWhenRoutesMissing() {
        MapboxClient client = clientResponding(req -> response(200, "{}"));
        assertThrows(IOException.class, () -> client.fetchRoute(0, 0, 0, 0));
    }

    @Test
    void fetchRouteErrorsWhenRoutesEmpty() {
        MapboxClient client = clientResponding(req -> response(200, "{\"routes\":[]}"));
        assertThrows(IOException.class, () -> client.fetchRoute(0, 0, 0, 0));
    }

    @Test
    void fetchRouteErrorsWhenMissingFields() {
        MapboxClient client = clientResponding(req -> response(200, "{\"routes\":[{}]}"));
        assertThrows(IOException.class, () -> client.fetchRoute(0, 0, 0, 0));
    }

    @Test
    void fetchPlaceNameHandlesStatusesAndExceptions() throws Exception {
        AtomicInteger counter = new AtomicInteger();
        MapboxClient client = clientResponding(req -> switch (counter.getAndIncrement()) {
            case 0 -> response(200, "{\"features\":[{\"place_name\":\"Toronto\"}]}");
            case 1 -> response(404, "");
            default -> new InterruptedException("fail");
        });

        assertEquals("Toronto", client.fetchPlaceName(0, 0));
        assertNull(client.fetchPlaceName(0, 0));
        assertNull(client.fetchPlaceName(0, 0));
    }

    @Test
    void fetchStaticMapSuccessWithRoute() throws Exception {
        MapboxClient client = clientResponding(req -> {
            try {
                return responseBytes(200, createPng());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        RequestLocation location = new RequestLocation("id", "title", 1, 2, 3, 4);

        BufferedImage image = client.fetchStaticMap(location,
                "{\"type\":\"LineString\",\"coordinates\":[[0,0],[1,1]]}");

        assertEquals(1, image.getWidth());
        assertEquals(1, image.getHeight());
    }

    @Test
    void fetchStaticMapStatusError() {
        MapboxClient client = clientResponding(req -> responseBytes(500, new byte[0]));
        RequestLocation location = new RequestLocation("id", "title", 1, 2, 3, 4);

        assertThrows(IOException.class, () -> client.fetchStaticMap(location, null));
    }

    @Test
    void fetchStaticMapDecodeError() {
        MapboxClient client = clientResponding(req -> responseBytes(200, "bad".getBytes()));
        RequestLocation location = new RequestLocation("id", "title", 1, 2, 3, 4);

        assertThrows(IOException.class, () -> client.fetchStaticMap(location, null));
    }

    @Test
    void fetchStaticMapHandlesInvalidRouteGeometry() throws Exception {
        MapboxClient client = clientResponding(req -> {
            try {
                return responseBytes(200, createPng());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        RequestLocation location = new RequestLocation("id", "title", 1, 2, 3, 4);

        BufferedImage image = client.fetchStaticMap(location, "not-json");
        assertEquals(1, image.getHeight());
    }

    @Test
    void defaultConstructorInitializesClient() {
        MapboxClient client = new MapboxClient("token");
        assertNotNull(client);
    }

    private static MapboxClient clientResponding(Function<HttpRequest, Object> responder) {
        return new MapboxClient("token", new StubHttpClient(responder));
    }

    private static HttpResponse<String> response(int status, String body) {
        return new TestHttpResponse<>(status, body);
    }

    private static HttpResponse<byte[]> responseBytes(int status, byte[] body) {
        return new TestHttpResponse<>(status, body);
    }

    private static String directionsJson(double durationSeconds) {
        return """
                {
                  \"routes\": [
                    {
                      \"duration\": %f,
                      \"geometry\": {\"type\":\"LineString\",\"coordinates\":[[0,0],[1,1]]}
                    }
                  ]
                }
                """.formatted(durationSeconds);
    }

    private static byte[] createPng() throws Exception {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, "png", out);
        return out.toByteArray();
    }

    private static final class StubHttpClient extends HttpClient {
        private final Function<HttpRequest, Object> responder;

        StubHttpClient(Function<HttpRequest, Object> responder) {
            this.responder = responder;
        }

        @Override
        public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler) throws IOException, InterruptedException {
            Object obj = responder.apply(request);
            if (obj instanceof IOException ioe) {
                throw ioe;
            }
            if (obj instanceof InterruptedException ie) {
                throw ie;
            }
            return (HttpResponse<T>) obj;
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> handler) {
            try {
                return CompletableFuture.completedFuture(send(request, handler));
            } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
            }
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request,
                                                               HttpResponse.BodyHandler<T> handler,
                                                               HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
            return sendAsync(request, handler);
        }

        @Override
        public Optional<CookieHandler> cookieHandler() {
            return Optional.empty();
        }

        @Override
        public Optional<Duration> connectTimeout() {
            return Optional.empty();
        }

        @Override
        public Redirect followRedirects() {
            return Redirect.NEVER;
        }

        @Override
        public Optional<ProxySelector> proxy() {
            return Optional.empty();
        }

        @Override
        public SSLContext sslContext() {
            return null;
        }

        @Override
        public SSLParameters sslParameters() {
            return new SSLParameters();
        }

        @Override
        public Optional<Authenticator> authenticator() {
            return Optional.empty();
        }

        @Override
        public HttpClient.Version version() {
            return HttpClient.Version.HTTP_1_1;
        }

        @Override
        public Optional<Executor> executor() {
            return Optional.empty();
        }
    }

    private static final class TestHttpResponse<T> implements HttpResponse<T> {
        private final int status;
        private final T body;

        TestHttpResponse(int status, T body) {
            this.status = status;
            this.body = body;
        }

        @Override
        public int statusCode() {
            return status;
        }

        @Override
        public HttpRequest request() {
            return HttpRequest.newBuilder(URI.create("https://example.com")).GET().build();
        }

        @Override
        public Optional<HttpResponse<T>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return HttpHeaders.of(java.util.Map.of(), (a, b) -> true);
        }

        @Override
        public T body() {
            return body;
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return URI.create("https://example.com");
        }

        @Override
        public HttpClient.Version version() {
            return HttpClient.Version.HTTP_1_1;
        }
    }
}
