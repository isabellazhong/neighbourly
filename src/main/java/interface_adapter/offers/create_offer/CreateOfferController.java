package interface_adapter.offers.create_offer;

import use_case.offers.create_offer.CreateOfferInputBoundary;
import use_case.offers.create_offer.CreateOfferInputData;

public class CreateOfferController {
    final CreateOfferInputBoundary createOfferInteractor;

    public CreateOfferController(CreateOfferInputBoundary createOfferInteractor) {
        this.createOfferInteractor = createOfferInteractor;
    }

    public void execute(String title, String details) {
        CreateOfferInputData inputData = new CreateOfferInputData(title, details);

        createOfferInteractor.execute(inputData);
    }
}
