package use_case.offers.edit_offers;

import java.util.UUID;

public class EditOfferInputData {
    private final UUID offerId;
    private final String title;
    private final String details;

    public EditOfferInputData(UUID offerId, String title, String details) {
        this.offerId = offerId;
        this.title = title;
        this.details = details;
    }

    public UUID getOfferId() { return offerId; }
    public String getTitle() { return title; }
    public String getDetails() { return details; }
}