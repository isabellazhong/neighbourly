package interface_adapter.messaging;

import java.util.UUID;

import use_case.messaging.MessagingInputBoundary;
import use_case.messaging.MessagingInputData;

public class MessagingController {
    private final MessagingInputBoundary messagingInputBoundary;

    public MessagingController(MessagingInputBoundary messagingInputBoundary) {
        this.messagingInputBoundary = messagingInputBoundary;
    }

    public void openChat(UUID requestId, UUID offerId, String userId, boolean isRequest) {
        MessagingInputData inputData = MessagingInputData.forOpenChat(requestId, offerId, userId, isRequest);
        messagingInputBoundary.openChat(inputData);
    }

    public void sendMessage(String channelId, String senderId, String messageText) {
        MessagingInputData inputData = MessagingInputData.forSendMessage(channelId, senderId, messageText);
        messagingInputBoundary.sendMessage(inputData);
    }

    public void refreshMessages(String channelId, long lastTimestamp) {
        MessagingInputData inputData = MessagingInputData.forRefresh(channelId, lastTimestamp);
        messagingInputBoundary.refreshMessages(inputData);
    }

    public void fetchAllMessages(String channelId) {
        MessagingInputData inputData = MessagingInputData.forRefresh(channelId, 0);
        messagingInputBoundary.refreshMessages(inputData);
    }
}
