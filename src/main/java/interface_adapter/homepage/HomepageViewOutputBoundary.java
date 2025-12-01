package interface_adapter.homepage;

import view.map.RequestLocation;

import java.util.List;

public interface HomepageViewOutputBoundary {
    void presentHome(List<RequestLocation> requests);
}
