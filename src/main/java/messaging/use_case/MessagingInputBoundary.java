package messaging.use_case;

public interface MessagingInputBoundary {
    void openChat(MessagingInputData inputData);
    void sendMessage(MessagingInputData inputData);
    void refreshMessages(MessagingInputData inputData);
}
