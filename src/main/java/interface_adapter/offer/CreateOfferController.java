package interface_adapter.offer;

import use_case.offer.CreateOfferInputBoundary;
import use_case.offer.CreateOfferInputData;

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
