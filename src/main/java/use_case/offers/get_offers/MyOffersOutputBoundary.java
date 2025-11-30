package use_case.offers.get_offers;

import java.util.List;
import entity.Offer;

public interface MyOffersOutputBoundary {
    void presentMyOffers(List<Offer> offers);
    void prepareFailView(String erorr);
}
