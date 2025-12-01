package use_case.offers.create_offer;

import entity.Offer;

public class CreateOfferOutputData {
    private final Offer offer;
    private final boolean useCaseFailed;

    public CreateOfferOutputData(Offer offer, boolean useCaseFailed) {
        this.offer = offer;
        this.useCaseFailed = useCaseFailed;
    }

    public Offer getOffer() {
        return offer;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
