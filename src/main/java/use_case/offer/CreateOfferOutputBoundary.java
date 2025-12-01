package use_case.offer;

public interface CreateOfferOutputBoundary {
    void prepareSuccessView(CreateOfferOutputData outputData);
    void prepareFailView(String error);
}
