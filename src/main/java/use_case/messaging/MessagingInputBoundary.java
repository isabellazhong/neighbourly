package use_case.messaging;

public interface MessagingInputBoundary {
    void openChat(MessagingInputData inputData);
    void sendMessage(MessagingInputData inputData);
    void refreshMessages(MessagingInputData inputData);
}
