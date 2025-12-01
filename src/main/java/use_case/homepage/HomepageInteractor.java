package use_case.homepage;

import interface_adapter.homepage.HomepageViewOutputBoundary;
import view.map.RequestLocation;

import java.util.List;

public class HomepageInteractor implements HomepageInputBoundary {
    private final HomepageViewOutputBoundary presenter;

    public HomepageInteractor(HomepageViewOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadHome() {
        // demo data kept consistent with existing UI
        double helperLat = 43.6617;
        double helperLng = -79.3950;
        List<RequestLocation> demo = List.of(
                new RequestLocation("REQ-001", "Grocery drop-off (Toronto)", 43.6532, -79.3832, helperLat, helperLng),
                new RequestLocation("REQ-002", "Medication pickup (Toronto)", 43.7000, -79.4000, helperLat, helperLng)
        );
        presenter.presentHome(demo);
    }

    @Override
    public void search(String query) {
        // simple demo: reuse loadHome or add filtering later
        loadHome();
    }
}
