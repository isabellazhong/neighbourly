package use_case.request;

import interface_adapter.homepage.HomepageViewOutputBoundary;
import entity.Request;

public class CreateRequestInteractor {
    private final CreatedRequestRepository repo;
    private final HomepageViewOutputBoundary presenter;

    public CreateRequestInteractor(CreatedRequestRepository repo, HomepageViewOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    public void execute(String title, String details, boolean service) {
        Request r = new Request(title, details, service, null);
        repo.addFirst(r);
        presenter.presentHome(null, repo.list());
    }
}
