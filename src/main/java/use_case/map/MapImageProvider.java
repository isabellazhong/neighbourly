package use_case.map;

import view.map.RequestLocation;

import java.awt.image.BufferedImage;

public interface MapImageProvider {
    BufferedImage loadImage(RequestLocation location, String routeGeoJson) throws Exception;
}
