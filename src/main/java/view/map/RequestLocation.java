package view.map;

/**
 * Simple data holder for a request and the two locations involved.
 */
public record RequestLocation(
        String requestId,
        String title,
        double requesterLat,
        double requesterLng,
        double helperLat,
        double helperLng
) {
}
