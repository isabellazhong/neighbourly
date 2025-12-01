package use_case.homepage;

import interface_adapter.homepage.HomepageViewOutputBoundary;
import view.map.RequestLocation;
import entity.Request;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class HomepageInteractor implements HomepageInputBoundary {
    private final HomepageViewOutputBoundary presenter;
    private final List<Request> createdRequests = new ArrayList<>();

    public HomepageInteractor(HomepageViewOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadHome() {
        double helperLat = 43.6617;
        double helperLng = -79.3950;
        List<RequestLocation> demo = List.of(
                new RequestLocation("REQ-001", "Grocery drop-off (Toronto)", 43.6532, -79.3832, helperLat, helperLng),
                new RequestLocation("REQ-002", "Medication pickup (Toronto)", 43.7000, -79.4000, helperLat, helperLng)
        );
        presenter.presentHome(demo, List.copyOf(createdRequests));
    }

    @Override
    public void search(String query) {
        // simple demo behaviour: re-use loadHome
        loadHome();
    }

    @Override
    public void createRequest(String title, String details, boolean service) {
        Request r = new Request(title, details, service, null);
        createdRequests.add(0, r);
        loadHome(); // notify presenter with updated created list
    }

    @Override
    public void editRequest(String requestId, String newTitle, String newDetails, boolean service) {
        try {
            UUID id = UUID.fromString(requestId);
            for (int i = 0; i < createdRequests.size(); i++) {
                Request r = createdRequests.get(i);
                if (r.getId().equals(id)) {
                    r.setTitle(newTitle);
                    r.setDetails(newDetails);
                    r.setService(service);
                    break;
                }
            }
            loadHome();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void deleteRequest(String requestId) {
        try {
            UUID id = UUID.fromString(requestId);
            createdRequests.removeIf(r -> r.getId().equals(id));
            loadHome();
        } catch (Exception ignored) {
        }
    }
}
