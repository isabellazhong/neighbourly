package use_case.offer;

import entity.Offer;

import java.util.Date;


public class CreateOfferInteractor implements CreateOfferInputBoundary {
    final OfferDataAccessInterface offerDataAccessObject;

    public CreateOfferInteractor(OfferDataAccessInterface offerDataAccessObject) {
        this.offerDataAccessObject = offerDataAccessObject;
    }

    @Override
    public void execute(CreateOfferInputData inputData) {
        Offer newOffer = new Offer(
                inputData.getTitle(),
                inputData.getDetails(),
                new Date()
        );
        offerDataAccessObject.addOffer(newOffer);

        System.out.println("Interactor: Offer saved to MongoDB with ID: " + newOffer.getId());
    }
}
