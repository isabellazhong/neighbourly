package use_case.request.accept_request;

public interface AcceptRequestOutputBoundary {
    void presentMessagingView(AcceptRequestOutputData outputData);

    void presentFailure(String errorMessage);
}
