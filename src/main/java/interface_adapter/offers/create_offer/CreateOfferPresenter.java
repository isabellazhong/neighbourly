package interface_adapter.offers.create_offer;

import use_case.offers.create_offer.CreateOfferOutputBoundary;
import use_case.offers.create_offer.CreateOfferOutputData;
import use_case.offers.create_offer.CreateOfferState;

public class CreateOfferPresenter implements CreateOfferOutputBoundary {
    private final CreateOfferViewModel createOfferViewModel;

    public CreateOfferPresenter(CreateOfferViewModel createOfferViewModel) {
        this.createOfferViewModel = createOfferViewModel;
    }

    @Override
    public void prepareSuccessView(CreateOfferOutputData outputData) {
        CreateOfferState state = createOfferViewModel.getState();
        state.setOfferError(null);
        state.setOfferSuccess("Offer Submitted Successfully");
        createOfferViewModel.setState(state);
        createOfferViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        CreateOfferState state = createOfferViewModel.getState();
        state.setOfferError(error);
        state.setOfferSuccess(null);
        createOfferViewModel.setState(state);
        createOfferViewModel.firePropertyChange();
    }
}
