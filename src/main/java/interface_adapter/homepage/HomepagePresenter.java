package interface_adapter.homepage;

import view.map.RequestLocation;
import entity.Request;

import java.util.List;

public class HomepagePresenter implements HomepageViewOutputBoundary {
    private final ViewModelManager manager;

    public HomepagePresenter(ViewModelManager manager) {
        this.manager = manager;
    }

    @Override
    public void presentHome(List<RequestLocation> requests) {
        presentHome(requests, null);
    }

    @Override
    public void presentHome(List<RequestLocation> requests, List<Request> createdRequests) {
        HomepageViewModel vm = new HomepageViewModel(requests, createdRequests);
        manager.setViewModel(vm);
    }
}
