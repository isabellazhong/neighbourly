package view.map;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Swing-only map view:
 * - Fetches ETA and shows a static map preview.
 * - Provides a button to open the fully interactive Mapbox GL map in the default browser.
 * This avoids JavaFX/WebGL issues on macOS.
 */
public class RequestMapView extends JPanel {

    private final JLabel etaLabel = new JLabel("Loading ETA…");
    private final JButton messageButton = new JButton("Message requester");
    private final JButton openBrowserButton = new JButton("Open in browser");
    private final JLabel mapLabel = new JLabel("Loading map…", SwingConstants.CENTER);
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel centerPanel = new JPanel(cardLayout);
    private JFXPanel jfxPanel;
    private WebEngine webEngine;
    private static volatile boolean fxInitialized = false;

    private final MapboxClient mapboxClient;
    private final RequestLocation requestLocation;
    private final String mapboxToken;

    public RequestMapView(String mapboxToken, RequestLocation requestLocation) {
        this.mapboxToken = mapboxToken;
        this.mapboxClient = new MapboxClient(mapboxToken);
        this.requestLocation = requestLocation;
        buildUi();
        loadData();
    }

    private void buildUi() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        mapLabel.setPreferredSize(new Dimension(900, 600));
        centerPanel.add(mapLabel, "static");
        add(centerPanel, BorderLayout.CENTER);

        JPanel infoBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        JLabel title = new JLabel(requestLocation.title());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        infoBar.add(title);

        etaLabel.setFont(etaLabel.getFont().deriveFont(Font.PLAIN, 14f));
        infoBar.add(etaLabel);

        messageButton.setEnabled(false);
        messageButton.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Opening messaging for request " + requestLocation.requestId(),
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
        infoBar.add(messageButton);

        openBrowserButton.addActionListener(openBrowserAction());
        infoBar.add(openBrowserButton);

        add(infoBar, BorderLayout.SOUTH);
    }

    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private BufferedImage mapImage;
            private double etaMinutes;
            private String routeGeometry;

            @Override
            protected Void doInBackground() throws Exception {
                MapboxClient.RouteInfo route = mapboxClient.fetchRoute(
                        requestLocation.helperLng(),
                        requestLocation.helperLat(),
                        requestLocation.requesterLng(),
                        requestLocation.requesterLat()
                );
                etaMinutes = route.etaMinutes();
                routeGeometry = route.geometryJson();
                mapImage = mapboxClient.fetchStaticMap(requestLocation, routeGeometry);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    etaLabel.setText("ETA: " + (int) etaMinutes + " min");
                    messageButton.setEnabled(true);
                    mapLabel.setText("");
                    mapLabel.setIcon(new ImageIcon(mapImage));
                } catch (Exception ex) {
                    etaLabel.setText("ETA unavailable");
                    messageButton.setEnabled(false);
                    mapLabel.setText("<html><div style='text-align:center;'>Map failed to load.<br/>"
                            + ex.getMessage() + "</div></html>");
                }
            }
        };
        worker.execute();

        // Try to embed interactive map; if it fails, the static map remains.
        tryEmbedInteractiveMap();
    }

    private ActionListener openBrowserAction() {
        return e -> {
            try {
                File temp = buildTempInteractiveHtml();
                Desktop.getDesktop().browse(temp.toURI());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Unable to open interactive map: " + ex.getMessage(),
                        "Map error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };
    }

    private void tryEmbedInteractiveMap() {
        try {
            ensureFxToolkit();
            jfxPanel = new JFXPanel();
            jfxPanel.setPreferredSize(new Dimension(900, 600));
            centerPanel.add(jfxPanel, "webview");
            cardLayout.show(centerPanel, "webview");

            File temp = buildTempInteractiveHtml();
            Platform.runLater(() -> {
                WebView webView = new WebView();
                webEngine = webView.getEngine();
                webEngine.load(temp.toURI().toString());
                jfxPanel.setScene(new Scene(webView));
            });
        } catch (Exception ex) {
            // Fall back to static view
            cardLayout.show(centerPanel, "static");
        }
    }

    private void ensureFxToolkit() {
        if (fxInitialized) return;
        synchronized (RequestMapView.class) {
            if (fxInitialized) return;
            Platform.startup(() -> {});
            fxInitialized = true;
        }
    }

    private File buildTempInteractiveHtml() throws Exception {
        String safeToken = mapboxToken.replace("\\", "\\\\").replace("\"", "\\\"");
        String html =
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Neighbourly Map</title>\n" +
                "  <link href=\"https://api.mapbox.com/mapbox-gl-js/v2.15.0/mapbox-gl.css\" rel=\"stylesheet\"/>\n" +
                "  <script src=\"https://api.mapbox.com/mapbox-gl-js/v2.15.0/mapbox-gl.js\"></script>\n" +
                "  <style>\n" +
                "    html, body { margin: 0; padding: 0; width: 100%; height: 100%; }\n" +
                "    #map { position: absolute; top: 0; bottom: 0; width: 100%; }\n" +
                "    #status {\n" +
                "      position: absolute;\n" +
                "      top: 12px;\n" +
                "      left: 12px;\n" +
                "      background: rgba(255, 255, 255, 0.9);\n" +
                "      padding: 6px 10px;\n" +
                "      border-radius: 6px;\n" +
                "      font-family: system-ui, -apple-system, sans-serif;\n" +
                "      font-size: 13px;\n" +
                "      box-shadow: 0 1px 4px rgba(0,0,0,0.12);\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"map\"></div>\n" +
                "<div id=\"status\">Loading…</div>\n" +
                "<script>\n" +
                "  const token = \"" + safeToken + "\";\n" +
                "  const helperLat = " + requestLocation.helperLat() + ";\n" +
                "  const helperLng = " + requestLocation.helperLng() + ";\n" +
                "  const reqLat = " + requestLocation.requesterLat() + ";\n" +
                "  const reqLng = " + requestLocation.requesterLng() + ";\n" +
                "\n" +
                "  mapboxgl.accessToken = token;\n" +
                "  const map = new mapboxgl.Map({\n" +
                "    container: 'map',\n" +
                "    style: 'mapbox://styles/mapbox/streets-v12',\n" +
                "    center: [helperLng, helperLat],\n" +
                "    zoom: 13\n" +
                "  });\n" +
                "  map.addControl(new mapboxgl.NavigationControl(), 'top-right');\n" +
                "  map.on('load', () => {\n" +
                "    addMarker(helperLng, helperLat, '#1d4ed8');\n" +
                "    addMarker(reqLng, reqLat, '#ef4444');\n" +
                "    fitToBounds(helperLng, helperLat, reqLng, reqLat);\n" +
                "    fetchRoute();\n" +
                "  });\n" +
                "\n" +
                "  function addMarker(lng, lat, color) {\n" +
                "    new mapboxgl.Marker({ color }).setLngLat([lng, lat]).addTo(map);\n" +
                "  }\n" +
                "  function fitToBounds(lng1, lat1, lng2, lat2) {\n" +
                "    const bounds = new mapboxgl.LngLatBounds();\n" +
                "    bounds.extend([lng1, lat1]);\n" +
                "    bounds.extend([lng2, lat2]);\n" +
                "    map.fitBounds(bounds, { padding: 60 });\n" +
                "  }\n" +
                "  function fetchRoute() {\n" +
                "    const url = `https://api.mapbox.com/directions/v5/mapbox/driving/${helperLng},${helperLat};${reqLng},${reqLat}?geometries=geojson&overview=full&access_token=${token}`;\n" +
                "    fetch(url).then(r => r.json()).then(data => {\n" +
                "      if (!data.routes || !data.routes.length) throw new Error('No routes');\n" +
                "      const route = data.routes[0];\n" +
                "      renderRoute(route.geometry);\n" +
                "      const etaMinutes = Math.round(route.duration / 60);\n" +
                "      setStatus(`ETA: ${etaMinutes} min`);\n" +
                "    }).catch(err => {\n" +
                "      setStatus('Route failed: ' + err.message);\n" +
                "    });\n" +
                "  }\n" +
                "  function renderRoute(geojson) {\n" +
                "    if (map.getSource('route')) {\n" +
                "      map.getSource('route').setData(geojson);\n" +
                "    } else {\n" +
                "      map.addSource('route', { type: 'geojson', data: geojson });\n" +
                "      map.addLayer({\n" +
                "        id: 'route-line',\n" +
                "        type: 'line',\n" +
                "        source: 'route',\n" +
                "        paint: { 'line-color': '#1d4ed8', 'line-width': 4 }\n" +
                "      });\n" +
                "    }\n" +
                "  }\n" +
                "  function setStatus(text) {\n" +
                "    const el = document.getElementById('status');\n" +
                "    if (el) el.textContent = text;\n" +
                "  }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";

        File temp = File.createTempFile("neighbourly-map", ".html");
        temp.deleteOnExit();
        try (FileWriter fw = new FileWriter(temp, StandardCharsets.UTF_8)) {
            fw.write(html);
        }
        return temp;
    }
}
