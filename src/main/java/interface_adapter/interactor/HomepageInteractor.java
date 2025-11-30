package interface_adapter.interactor;

import interface_adapter.navigator.HomepageViewOutputBoundary;

public class HomepageInteractor implements HomepageInputBoundary {
    private final HomepageViewOutputBoundary presenter;

    public HomepageInteractor(HomepageViewOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void handle(HomepageInputData inputData) {
        if (inputData == null || inputData.getAction() == null) return;
        switch (inputData.getAction()) {
            case CREATE_OFFER:
                presenter.prepareOfferPage();
                break;
            case CREATE_REQUEST:
                presenter.prepareRequestPage();
                break;
            default:
                // no-op
        }
    }
}
