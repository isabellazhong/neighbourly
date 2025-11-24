package use_case.offer;

import entity.Offer;
import database.MongoDBOfferDataAccessObject;

import java.util.Date;


public class CreateOfferInteractor implements CreateOfferInputBoundary {
    final MongoDBOfferDataAccessObject offerDataAccessObject;

    public CreateOfferInteractor(MongoDBOfferDataAccessObject offerDataAccessObject) {
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
