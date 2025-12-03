package use_case.request;

import interface_adapter.homepage.HomepageViewOutputBoundary;
import java.util.UUID;

public class DeleteRequestInteractor {
    private final CreatedRequestRepository repo;
    private final HomepageViewOutputBoundary presenter;

    public DeleteRequestInteractor(CreatedRequestRepository repo, HomepageViewOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    public void execute(String requestId) {
        try {
            UUID id = UUID.fromString(requestId);
            boolean removed = repo.remove(id);
            if (removed) presenter.presentHome(null, repo.list());
        } catch (Exception ignored) {}
    }
}
