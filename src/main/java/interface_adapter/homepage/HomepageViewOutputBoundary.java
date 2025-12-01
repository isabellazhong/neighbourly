package interface_adapter.homepage;

import view.map.RequestLocation;
import entity.Request;

import java.util.List;

public interface HomepageViewOutputBoundary {
    void presentHome(List<RequestLocation> requests);
    void presentHome(List<RequestLocation> requests, List<Request> createdRequests);
}

