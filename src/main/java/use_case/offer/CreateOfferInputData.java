package use_case.offer;

public class CreateOfferInputData {
    private final String title;
    private final String details;

    public CreateOfferInputData(String title, String details) {
        this.title = title;
        this.details = details;
    }
    public String getTitle() {
        return title;
    }
    public String getDetails() {
        return details;
    }
}
