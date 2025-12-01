package use_case.offers.create_offer;

public class CreateOfferState {
    private String title = "";
    private String details = "";
    private String offerError = null;
    private String offerSuccess = null;

    public CreateOfferState() {
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public String getOfferError() {
        return offerError;
    }
    public void setOfferError(String offerError) {
        this.offerError = offerError;
    }

    public String getOfferSuccess() {
        return offerSuccess;
    }

    public void setOfferSuccess(String offerSuccess) {
        this.offerSuccess = offerSuccess;
    }
}
