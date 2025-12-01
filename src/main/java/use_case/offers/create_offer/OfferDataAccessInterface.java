package use_case.offers.create_offer;
import entity.Offer;
import java.util.List;
import java.util.UUID;

public interface OfferDataAccessInterface {
    void addOffer(Offer offer);
    String getChatChannelId(UUID offerId);
    void setChatChannelId(UUID offerId, String channelId);
    void setAccepted(UUID offerId, boolean accepted);
    List<Offer> allOffers();
    List<Offer> myOffers(String username);
}
