package use_case.messaging;

public interface MessagingOutputBoundary {
    void prepareSuccessView(MessagingOutputData outputData);
    void prepareFailView(String error);
}
