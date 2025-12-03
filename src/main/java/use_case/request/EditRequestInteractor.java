package use_case.request;

import interface_adapter.homepage.HomepageViewOutputBoundary;
import java.util.UUID;

public class EditRequestInteractor {
    private final CreatedRequestRepository repo;
    private final HomepageViewOutputBoundary presenter;

    public EditRequestInteractor(CreatedRequestRepository repo, HomepageViewOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    public void execute(String requestId, String newTitle, String newDetails, boolean service) {
        try {
            UUID id = UUID.fromString(requestId);
            boolean updated = repo.update(id, newTitle, newDetails, service);
            if (updated) presenter.presentHome(null, repo.list());
        } catch (Exception ignored) {}
    }
}
