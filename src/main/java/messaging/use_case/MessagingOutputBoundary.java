package messaging.use_case;

public interface MessagingOutputBoundary {
    void prepareSuccessView(MessagingOutputData outputData);
    void prepareFailView(String error);
}
