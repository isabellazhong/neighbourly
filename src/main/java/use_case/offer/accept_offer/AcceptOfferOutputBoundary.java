package use_case.offer.accept_offer;

public interface AcceptOfferOutputBoundary {
    void presentMessagingView(AcceptOfferOutputData outputData);

    void presentFailure(String errorMessage);
}
