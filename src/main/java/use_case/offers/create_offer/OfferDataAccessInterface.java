package use_case.offers.create_offer;
import entity.Offer;
import java.util.List;

public interface OfferDataAccessInterface {
    void addOffer(Offer offer);
    List<Offer> AllOffers();
    List<Offer> MyOffers(String username);
}
