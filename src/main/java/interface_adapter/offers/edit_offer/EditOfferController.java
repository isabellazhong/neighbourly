package interface_adapter.offers.edit_offer;

import use_case.offers.edit_offers.EditOfferInputBoundary;
import use_case.offers.edit_offers.EditOfferInputData;
import java.util.UUID;

public class EditOfferController {
    final EditOfferInputBoundary editOfferUseCaseInteractor;

    public EditOfferController(EditOfferInputBoundary editOfferUseCaseInteractor) {
        this.editOfferUseCaseInteractor = editOfferUseCaseInteractor;
    }

    public void execute(UUID offerId, String title, String details) {
        EditOfferInputData inputData = new EditOfferInputData(offerId, title, details);
        editOfferUseCaseInteractor.execute(inputData);
    }
}