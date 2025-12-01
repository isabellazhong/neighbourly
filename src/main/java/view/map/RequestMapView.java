package view.map;

import use_case.map.MapImageProvider;
import use_case.map.MapService;

import javax.swing.*;
import java.awt.*;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
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
    private final JLabel helperAddrLabel = new JLabel("Helper: resolving address…");
    private final JLabel requesterAddrLabel = new JLabel("Requester: resolving address…");
    private final JButton openBrowserButton = new JButton("Open in browser");
    private final JButton messageButton = new JButton("Message requester");
    private final JLabel mapLabel = new JLabel("Loading map…", SwingConstants.CENTER);
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel centerPanel = new JPanel(cardLayout);

    private final MapService mapService;
    private final MapImageProvider mapImageProvider;
    private final RequestLocation requestLocation;
    private final String mapboxToken;
    private final BrowserLauncher browserLauncher;
    private final MessageReporter messageReporter;
    private SwingWorker<Void, Void> worker;

    public RequestMapView(MapService mapService,
                          MapImageProvider mapImageProvider,
                          String mapboxToken,
                          RequestLocation requestLocation) {
        this(mapService, mapImageProvider, mapboxToken, requestLocation,
                RequestMapView::defaultBrowse,
                RequestMapView::defaultShowError);
    }

    RequestMapView(MapService mapService,
                   MapImageProvider mapImageProvider,
                   String mapboxToken,
                   RequestLocation requestLocation,
                   BrowserLauncher browserLauncher,
                   MessageReporter messageReporter) {
        this.mapboxToken = mapboxToken;
        this.mapService = mapService;
        this.mapImageProvider = mapImageProvider;
        this.requestLocation = requestLocation;
        this.browserLauncher = browserLauncher;
        this.messageReporter = messageReporter;
        setPreferredSize(new Dimension(1100, 780));
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

        helperAddrLabel.setFont(helperAddrLabel.getFont().deriveFont(Font.PLAIN, 12f));
        requesterAddrLabel.setFont(requesterAddrLabel.getFont().deriveFont(Font.PLAIN, 12f));
        JPanel addresses = new JPanel();
        addresses.setLayout(new BoxLayout(addresses, BoxLayout.Y_AXIS));
        addresses.setOpaque(false);
        addresses.add(helperAddrLabel);
        addresses.add(requesterAddrLabel);
        infoBar.add(addresses);

        openBrowserButton.addActionListener(openBrowserAction());
        infoBar.add(openBrowserButton);

        messageButton.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Messaging not implemented in this demo.",
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
        infoBar.add(messageButton);

        add(infoBar, BorderLayout.SOUTH);
    }

    private void loadData() {
        worker = new SwingWorker<>() {
            private BufferedImage mapImage;
            private double etaWalk;
            private double etaBike;
            private double etaDrive;
            private String routeGeometry;
            private String helperAddress;
            private String requesterAddress;

            @Override
            protected Void doInBackground() throws Exception {
                MapService.RouteInfo drive = mapService.getRoute("driving",
                        requestLocation.helperLng(), requestLocation.helperLat(),
                        requestLocation.requesterLng(), requestLocation.requesterLat());
                MapService.RouteInfo walk = mapService.getRoute("walking",
                        requestLocation.helperLng(), requestLocation.helperLat(),
                        requestLocation.requesterLng(), requestLocation.requesterLat());
                MapService.RouteInfo bike = mapService.getRoute("cycling",
                        requestLocation.helperLng(), requestLocation.helperLat(),
                        requestLocation.requesterLng(), requestLocation.requesterLat());

                etaDrive = drive.etaMinutes();
                etaWalk = walk.etaMinutes();
                etaBike = bike.etaMinutes();
                routeGeometry = drive.geometryJson();
                mapImage = mapImageProvider.loadImage(requestLocation, routeGeometry);
                helperAddress = mapService.reverseGeocode(requestLocation.helperLng(), requestLocation.helperLat());
                requesterAddress = mapService.reverseGeocode(requestLocation.requesterLng(), requestLocation.requesterLat());
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    etaLabel.setText(String.format("Walk: %d min   Bike: %d min   Car: %d min",
                            (int) etaWalk, (int) etaBike, (int) etaDrive));
                    helperAddrLabel.setText("Helper: " + (helperAddress != null ? helperAddress :
                            String.format("(%.5f, %.5f)", requestLocation.helperLat(), requestLocation.helperLng())));
                    requesterAddrLabel.setText("Requester: " + (requesterAddress != null ? requesterAddress :
                            String.format("(%.5f, %.5f)", requestLocation.requesterLat(), requestLocation.requesterLng())));
                    mapLabel.setText("");
                    mapLabel.setIcon(new ImageIcon(mapImage));
                } catch (Exception ex) {
                    etaLabel.setText("ETA unavailable");
                    mapLabel.setText("<html><div style='text-align:center;'>Map failed to load.<br/>"
                            + ex.getMessage() + "</div></html>");
                }
            }
        };
        worker.execute();
    }

    // Test helpers
    void waitForLoad() {
        if (worker == null) return;
        try {
            worker.get(3, java.util.concurrent.TimeUnit.SECONDS);
            // Flush EDT tasks (e.g., SwingWorker.done)
            SwingUtilities.invokeAndWait(() -> { /* no-op */ });
        } catch (Exception ignored) {
        }
    }

    String getEtaLabelText() {
        return etaLabel.getText();
    }

    String getHelperAddressText() {
        return helperAddrLabel.getText();
    }

    String getRequesterAddressText() {
        return requesterAddrLabel.getText();
    }

    javax.swing.Icon getMapIcon() {
        return mapLabel.getIcon();
    }

    String getMapLabelText() {
        return mapLabel.getText();
    }

    private ActionListener openBrowserAction() {
        return e -> {
            try {
                File temp = buildTempInteractiveHtml();
                browserLauncher.open(temp.toURI());
            } catch (Exception ex) {
                messageReporter.show(
                        this,
                        "Unable to open interactive map: " + ex.getMessage(),
                        "Map error"
                );
            }
        };
    }

    private File buildTempInteractiveHtml() throws Exception {
        String safeToken = (mapboxToken != null ? mapboxToken : "")
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        String helperAddress = mapService.reverseGeocode(requestLocation.helperLng(), requestLocation.helperLat());
        String requesterAddress = mapService.reverseGeocode(requestLocation.requesterLng(), requestLocation.requesterLat());
        String helperAddressSafe = helperAddress != null ? helperAddress.replace("\"", "\\\"") :
                String.format("(%.5f, %.5f)", requestLocation.helperLat(), requestLocation.helperLng());
        String requesterAddressSafe = requesterAddress != null ? requesterAddress.replace("\"", "\\\"") :
                String.format("(%.5f, %.5f)", requestLocation.requesterLat(), requestLocation.requesterLng());
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
                "<div id=\"addresses\" style=\"position:absolute;bottom:12px;left:12px;background:rgba(255,255,255,0.9);padding:6px 10px;border-radius:6px;font-family:system-ui;font-size:12px;box-shadow:0 1px 4px rgba(0,0,0,0.12);\">\n" +
                "  <div id=\"addr-helper\"></div>\n" +
                "  <div id=\"addr-requester\"></div>\n" +
                "</div>\n" +
                "<script>\n" +
                "  const token = \"" + safeToken + "\";\n" +
                "  const helperLat = " + requestLocation.helperLat() + ";\n" +
                "  const helperLng = " + requestLocation.helperLng() + ";\n" +
                "  const reqLat = " + requestLocation.requesterLat() + ";\n" +
                "  const reqLng = " + requestLocation.requesterLng() + ";\n" +
                "  const helperAddress = \"" + helperAddressSafe + "\";\n" +
                "  const requesterAddress = \"" + requesterAddressSafe + "\";\n" +
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
                "    fetchRoutes();\n" +
                "    setAddresses();\n" +
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
                "  async function fetchRoutes() {\n" +
                "    try {\n" +
                "      const profiles = ['driving','walking','cycling'];\n" +
                "      const results = await Promise.all(profiles.map(p => fetchDirections(p)));\n" +
                "      const drive = results[0];\n" +
                "      if (drive && drive.geometry) renderRoute(drive.geometry);\n" +
                "      const etaParts = [];\n" +
                "      if (results[1]) etaParts.push(`Walk: ${Math.round(results[1].duration/60)} min`);\n" +
                "      if (results[2]) etaParts.push(`Bike: ${Math.round(results[2].duration/60)} min`);\n" +
                "      if (results[0]) etaParts.push(`Car: ${Math.round(results[0].duration/60)} min`);\n" +
                "      setStatus(etaParts.length ? etaParts.join('  |  ') : 'ETA unavailable');\n" +
                "    } catch (err) {\n" +
                "      setStatus('Route failed: ' + err.message);\n" +
                "    }\n" +
                "  }\n" +
                "  async function fetchDirections(profile) {\n" +
                "    const url = `https://api.mapbox.com/directions/v5/mapbox/${profile}/${helperLng},${helperLat};${reqLng},${reqLat}?geometries=geojson&overview=full&access_token=${token}`;\n" +
                "    const r = await fetch(url);\n" +
                "    const data = await r.json();\n" +
                "    if (!data.routes || !data.routes.length) return null;\n" +
                "    return data.routes[0];\n" +
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
                "  function setAddresses() {\n" +
                "    const h = document.getElementById('addr-helper');\n" +
                "    const r = document.getElementById('addr-requester');\n" +
                "    if (h) h.textContent = 'Helper: ' + helperAddress;\n" +
                "    if (r) r.textContent = 'Requester: ' + requesterAddress;\n" +
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

    void triggerOpenInBrowser() {
        for (ActionListener listener : openBrowserButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "test"));
        }
    }

    @FunctionalInterface
    interface BrowserLauncher {
        void open(URI uri) throws Exception;
    }

    @FunctionalInterface
    interface MessageReporter {
        void show(Component parent, String message, String title);
    }

    public static void defaultBrowse(URI uri) throws Exception {
        if (!Desktop.isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop browse not supported");
        }
        Desktop.getDesktop().browse(uri);
    }

    public static void defaultShowError(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
