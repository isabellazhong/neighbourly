package interface_adapter.controller;

import interface_adapter.interactor.HomepageInputBoundary;
import interface_adapter.interactor.HomepageInputData;


public class HomepageController {
    private final HomepageInputBoundary interactor;

    public HomepageController(HomepageInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void onCreateRequestClicked() {
        interactor.handle(new HomepageInputData(HomepageInputData.Action.CREATE_REQUEST));
    }

    public void onCreateOfferClicked() {
        interactor.handle(new HomepageInputData(HomepageInputData.Action.CREATE_OFFER));
    }
}
