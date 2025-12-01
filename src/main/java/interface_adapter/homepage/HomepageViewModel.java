package interface_adapter.homepage;

import view.map.RequestLocation;

import java.util.List;

public class HomepageViewModel {
    private final List<RequestLocation> requests;

    public HomepageViewModel(List<RequestLocation> requests) {
        this.requests = requests;
    }

    public List<RequestLocation> getRequests() {
        return requests;
    }
}
