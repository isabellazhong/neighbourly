package interface_adapter.map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import view.map.MapboxClient;
import view.map.RequestLocation;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

class RealMapImageProviderTest {

    @Test
    void delegatesToClient() throws Exception {
        MapboxClient client = Mockito.mock(MapboxClient.class);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        RequestLocation location = new RequestLocation("id", "title", 1, 2, 3, 4);
        when(client.fetchStaticMap(location, "route")).thenReturn(image);

        RealMapImageProvider provider = new RealMapImageProvider(client);
        BufferedImage result = provider.loadImage(location, "route");

        assertSame(image, result);
    }

    @Test
    void defaultConstructorInitializesClient() {
        RealMapImageProvider provider = new RealMapImageProvider("token");
        assertNotNull(provider);
    }
}

