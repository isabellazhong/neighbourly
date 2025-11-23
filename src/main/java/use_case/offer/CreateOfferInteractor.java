package use_case.offer;

import entity.Offer;
import database.MongoDB;
import java.util.Date;


public class CreateOfferInteractor implements CreateOfferInputBoundary {
    final MongoDB offerDataAccessObject;

    public CreateOfferInteractor(MongoDB offerDataAccessObject) {
        this.offerDataAccessObject = offerDataAccessObject;
    }

    @Override
    public void execute(CreateOfferInputData inputData) {
        Offer newOffer = new Offer(
                inputData.getTitle(),
                inputData.getDetails(),
                new Date()
        );
        // add current user's id

        //save it to database
        offerDataAccessObject.addOffer(newOffer);

        System.out.println("Interactor: Offer saved to MongoDB with ID: " + newOffer.getId());
    }
}
