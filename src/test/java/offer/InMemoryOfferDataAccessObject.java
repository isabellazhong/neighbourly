package offer;

import entity.Offer;
import use_case.offers.create_offer.OfferDataAccessInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryOfferDataAccessObject implements OfferDataAccessInterface {
    private final List<Offer> offers = new ArrayList<>();

    @Override
    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    @Override
    public List<Offer>allOffers() {
        return offers;
    }

    @Override
    public List<Offer>myOffers(String username) {
        List<Offer> result = new ArrayList<>();
        for (Offer offer : offers) {
            if(offer.getUserID() != null && offer.getUserID().toString().equals(username)) {
                result.add(offer);
            }
        }
        return result;
    }

    @Override
    public Offer getOfferByID(UUID offerId) {
        for (Offer offer : offers) {
            if (offer.getId().equals(offerId)) {
                return offer;
            }
        }
        return null;
    }

    @Override
    public void updateOffer(Offer offer) {
        for (int i = 0; i < offers.size(); i++) {
            if (offers.get(i).getId().equals(offer.getId())) {
                offers.set(i, offer); // Replace the old one with the updated one
                return;
            }
        }
    }

    @Override
    public String getChatChannelId(java.util.UUID offerId) {
        return null;
    }

    @Override
    public void setChatChannelId(java.util.UUID offerId, String channelId) {
    }

    @Override
    public void setAccepted(java.util.UUID offerId, boolean accepted) {
        for (Offer offer : offers) {
            if(offer.getId().equals(offerId)) {
                offer.setAccepted(accepted);
                break;
            }
        }
    }
}
