package interface_adapter.homepage;

import view.map.RequestLocation;
import entity.Request;

import java.util.List;

public class HomepageViewModel {
    private final List<RequestLocation> nearbyRequests;
    private final List<Request> createdRequests;

    public HomepageViewModel(List<RequestLocation> nearbyRequests, List<Request> createdRequests) {
        this.nearbyRequests = nearbyRequests;
        this.createdRequests = createdRequests;
    }

    public List<RequestLocation> getRequests() {
        return nearbyRequests;
    }

    public List<Request> getCreatedRequests() {
        return createdRequests;
    }
}
