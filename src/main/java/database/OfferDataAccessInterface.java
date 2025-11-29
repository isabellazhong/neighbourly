package database;

import java.util.UUID;

public interface OfferDataAccessInterface {

    String getChatChannelId(UUID offerId);

    void setChatChannelId(UUID offerId, String channelId);

    void setAccepted(UUID offerId, boolean accepted);

}

