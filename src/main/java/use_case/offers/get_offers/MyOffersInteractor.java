package use_case.offers.get_offers;

import app.UserSession;
import entity.Offer;
import use_case.offers.create_offer.OfferDataAccessInterface;
import java.util.List;

public class MyOffersInteractor implements MyOffersInputBoundary {
    final OfferDataAccessInterface offerDataAccessObject;
    final MyOffersOutputBoundary presenter;

    public MyOffersInteractor(OfferDataAccessInterface offerDataAccessObject,
                              MyOffersOutputBoundary presenter) {
        this.offerDataAccessObject = offerDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        List<Offer> offers = offerDataAccessObject.allOffers();
        presenter.presentMyOffers(offers);
    }
}
