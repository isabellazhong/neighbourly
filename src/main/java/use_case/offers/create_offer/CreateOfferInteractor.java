package use_case.offers.create_offer;

import entity.Offer;

import java.util.Date;


public class CreateOfferInteractor implements CreateOfferInputBoundary {
    final OfferDataAccessInterface offerDataAccessObject;
    final CreateOfferOutputBoundary offerPresenter;

    public CreateOfferInteractor(OfferDataAccessInterface offerDataAccessObject,
                                 CreateOfferOutputBoundary offerPresenter) {
        this.offerDataAccessObject = offerDataAccessObject;
        this.offerPresenter = offerPresenter;
    }

    @Override
    public void execute(CreateOfferInputData inputData) {
        Offer newOffer = new Offer(
                inputData.getTitle(),
                inputData.getDetails(),
                new Date()
        );
        offerDataAccessObject.addOffer(newOffer);
        CreateOfferOutputData outputData = new CreateOfferOutputData(newOffer, false);
        offerPresenter.prepareSuccessView(outputData);
    }
}
