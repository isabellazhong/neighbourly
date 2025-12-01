package use_case.offers.get_offers;

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
        String username = "testUser";
        List<Offer> offers = offerDataAccessObject.MyOffers(username);
        presenter.presentMyOffers(offers);
    }
}
