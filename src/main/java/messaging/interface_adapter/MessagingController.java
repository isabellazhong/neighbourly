package messaging.interface_adapter;

import messaging.use_case.MessagingInputBoundary;
import messaging.use_case.MessagingInputData;

import java.util.UUID;

public class MessagingController {
    private final MessagingInputBoundary messagingInputBoundary;

    public MessagingController(MessagingInputBoundary messagingInputBoundary) {
        this.messagingInputBoundary = messagingInputBoundary;
    }

    public void openChat(UUID requestId, UUID offerId, String userId, boolean isRequest) {
        MessagingInputData inputData = new MessagingInputData(requestId, offerId, userId, isRequest);
        messagingInputBoundary.openChat(inputData);
    }

    public void sendMessage(String channelId, String senderId, String messageText) {
        MessagingInputData inputData = new MessagingInputData(channelId, senderId, messageText);
        messagingInputBoundary.sendMessage(inputData);
    }

    public void refreshMessages(String channelId, long lastTimestamp) {
        MessagingInputData inputData = new MessagingInputData(channelId, lastTimestamp);
        messagingInputBoundary.refreshMessages(inputData);
    }

    public void fetchAllMessages(String channelId) {
        // Fetch all messages by using timestamp 0
        MessagingInputData inputData = new MessagingInputData(channelId, 0);
        messagingInputBoundary.refreshMessages(inputData);
    }
}
