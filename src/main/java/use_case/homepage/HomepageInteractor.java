package use_case.homepage;

import interface_adapter.homepage.HomepageViewOutputBoundary;
import use_case.request.CreatedRequestRepository;
import use_case.request.InMemoryCreatedRequestRepository;
import use_case.request.CreateRequestInteractor;
import use_case.request.EditRequestInteractor;
import use_case.request.DeleteRequestInteractor;
import view.map.RequestLocation;
import java.util.List;

public class HomepageInteractor implements HomepageInputBoundary {
    private final HomepageViewOutputBoundary presenter;
    private final CreatedRequestRepository repo;
    private final CreateRequestInteractor createInteractor;
    private final EditRequestInteractor editInteractor;
    private final DeleteRequestInteractor deleteInteractor;

    public HomepageInteractor(HomepageViewOutputBoundary presenter) {
        this(presenter, new InMemoryCreatedRequestRepository());
    }

    public HomepageInteractor(HomepageViewOutputBoundary presenter, CreatedRequestRepository repo) {
        this.presenter = presenter;
        this.repo = repo;
        this.createInteractor = new CreateRequestInteractor(repo, presenter);
        this.editInteractor = new EditRequestInteractor(repo, presenter);
        this.deleteInteractor = new DeleteRequestInteractor(repo, presenter);
    }

    @Override
    public void loadHome() {
        double helperLat = 43.6617;
        double helperLng = -79.3950;
        List<RequestLocation> demo = List.of(
                new RequestLocation("REQ-001", "Grocery drop-off (Toronto)", 43.6532, -79.3832, helperLat, helperLng),
                new RequestLocation("REQ-002", "Medication pickup (Toronto)", 43.7000, -79.4000, helperLat, helperLng)
        );
        presenter.presentHome(demo, repo.list());
    }

    @Override
    public void createRequest(String title, String details, boolean service) {
        createInteractor.execute(title, details, service);
    }

    @Override
    public void editRequest(String requestId, String newTitle, String newDetails, boolean service) {
        editInteractor.execute(requestId, newTitle, newDetails, service);
    }

    @Override
    public void deleteRequest(String requestId) {
        deleteInteractor.execute(requestId);
    }
}
