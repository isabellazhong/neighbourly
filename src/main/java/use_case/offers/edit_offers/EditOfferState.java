package use_case.offers.edit_offers;

public class EditOfferState {
    private String title = "";
    private String details = "";
    private String error = null;
    private String successMessage = null;

    public String getTitle() { return title; }
    public String getDetails() { return details; }
    public String getError() { return error; }
    public String getSuccessMessage() { return successMessage; }

    public void setTitle(String title) { this.title = title; }
    public void setDetails(String details) { this.details = details; }
    public void setError(String error) { this.error = error; }
    public void setSuccessMessage(String successMessage) { this.successMessage = successMessage; }
}