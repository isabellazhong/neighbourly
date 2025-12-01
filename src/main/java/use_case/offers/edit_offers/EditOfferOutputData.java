package use_case.offers.edit_offers;

import entity.Offer;

public class EditOfferOutputData {
    private final Offer offer;

    public EditOfferOutputData(Offer offer) {
        this.offer = offer;
    }

    public Offer getOffer() { return offer; }
}
