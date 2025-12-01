package use_case.offer;
import java.util.UUID;

import entity.Offer;

public interface OfferDataAccessInterface {
    void addOffer(Offer offer);
    
    String getChatChannelId(UUID offerId);

    void setChatChannelId(UUID offerId, String channelId);

    void setAccepted(UUID offerId, boolean accepted);
}
