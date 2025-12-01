package interface_adapter.homepage;

import use_case.homepage.HomepageInputBoundary;

public class HomepageController {
    private final HomepageInputBoundary interactor;

    public HomepageController(HomepageInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadHome() {
        interactor.loadHome();
    }

    public void search(String query) {
        interactor.search(query);
    }
}
