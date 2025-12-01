package use_case.offers.create_offer;
import entity.Offer;
import java.util.List;

public interface OfferDataAccessInterface {
    void addOffer(Offer offer);
    List<Offer> allOffers();
    List<Offer> myOffers(String username);
}
