package interface_adapter.offers.edit_offer;

import use_case.offers.edit_offers.EditOfferOutputBoundary;
import use_case.offers.edit_offers.EditOfferOutputData;
import use_case.offers.edit_offers.EditOfferState;

public class EditOfferPresenter implements EditOfferOutputBoundary {
    private final EditOfferViewModel editOfferViewModel;

    public EditOfferPresenter(EditOfferViewModel editOfferViewModel) {
        this.editOfferViewModel = editOfferViewModel;
    }

    @Override
    public void prepareSuccessView(EditOfferOutputData outputData) {
        EditOfferState state = editOfferViewModel.getState();
        state.setSuccessMessage("Offer updated successfully!");
        state.setError(null);

        editOfferViewModel.setState(state);
        editOfferViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        EditOfferState state = editOfferViewModel.getState();
        state.setError(error);
        editOfferViewModel.setState(state);
        editOfferViewModel.firePropertyChange();
    }
}
