package interface_adapter.homepage;

import view.map.RequestLocation;

import java.util.List;

public class HomepagePresenter implements HomepageViewOutputBoundary {
    private final ViewModelManager manager;

    public HomepagePresenter(ViewModelManager manager) {
        this.manager = manager;
    }

    @Override
    public void presentHome(List<RequestLocation> requests) {
        HomepageViewModel vm = new HomepageViewModel(requests);
        manager.setViewModel(vm);
    }
}
