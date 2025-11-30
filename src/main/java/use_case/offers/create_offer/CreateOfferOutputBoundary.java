package use_case.offers.create_offer;

public interface CreateOfferOutputBoundary {
    void prepareSuccessView(CreateOfferOutputData outputData);
    void prepareFailView(String error);
}
