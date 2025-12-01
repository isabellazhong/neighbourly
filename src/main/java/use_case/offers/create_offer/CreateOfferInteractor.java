package use_case.offers.create_offer;

import app.UserSession;
import entity.Offer;
import entity.User;
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
        User currentUser = UserSession.getInstance().getCurrentUser();
        Offer newOffer = new Offer(
                inputData.getTitle(),
                inputData.getDetails(),
                new Date()
        );
        if (currentUser != null) {
            newOffer.setUserID(currentUser.getID());
        }
        offerDataAccessObject.addOffer(newOffer);
        CreateOfferOutputData outputData = new CreateOfferOutputData(newOffer, false);
        offerPresenter.prepareSuccessView(outputData);
    }
}
