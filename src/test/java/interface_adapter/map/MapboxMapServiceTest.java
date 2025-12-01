package interface_adapter.map;

import org.junit.jupiter.api.Test;
import view.map.MapboxClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MapboxMapServiceTest {

    @Test
    void wrapsMapboxClientValues() throws Exception {
        MapboxClient client = mock(MapboxClient.class);
        when(client.fetchRoute(eq("cycling"), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(new MapboxClient.RouteInfo(7, "geom"));
        when(client.fetchPlaceName(1.0, 2.0)).thenReturn("addr");

        MapboxMapService service = new MapboxMapService(client);
        MapboxMapService.RouteInfo route = service.getRoute("cycling", 0, 0, 0, 0);

        assertEquals(7, route.etaMinutes());
        assertEquals("geom", route.geometryJson());
        assertEquals("addr", service.reverseGeocode(1.0, 2.0));

        verify(client).fetchRoute(eq("cycling"), anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void defaultConstructorInstantiatesClient() {
        MapboxMapService service = new MapboxMapService("token");
        assertNotNull(service);
    }
}

