package use_case.offers.edit_offers;

public interface EditOfferOutputBoundary {
    void prepareSuccessView(EditOfferOutputData outputData);
    void prepareFailView(String error);
}
