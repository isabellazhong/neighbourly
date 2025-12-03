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

    public void createRequest(String title, String details, boolean service) {
        interactor.createRequest(title, details, service);
    }

    public void editRequest(String requestId, String newTitle, String newDetails, boolean service) {
        interactor.editRequest(requestId, newTitle, newDetails, service);
    }

    public void deleteRequest(String requestId) {
        interactor.deleteRequest(requestId);
    }
}
