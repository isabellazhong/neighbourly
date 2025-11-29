package use_case.messaging;

import java.util.UUID;

public class MessagingInputData {
    // For openChat
    private UUID requestId;
    private UUID offerId;
    private String userId;
    private boolean isRequest;
    
    // For sendMessage
    private String channelId;
    private String senderId;
    private String messageText;
    
    // For refreshMessages
    private long lastTimestamp;
    
    private MessagingInputData(UUID requestId, UUID offerId, String userId, boolean isRequest,
                              String channelId, String senderId, String messageText,
                              long lastTimestamp) {
        this.requestId = requestId;
        this.offerId = offerId;
        this.userId = userId;
        this.isRequest = isRequest;
        this.channelId = channelId;
        this.senderId = senderId;
        this.messageText = messageText;
        this.lastTimestamp = lastTimestamp;
    }
    
    public static MessagingInputData forOpenChat(UUID requestId, UUID offerId, String userId, boolean isRequest) {
        return new MessagingInputData(requestId, offerId, userId, isRequest,
                null, null, null, 0);
    }

    public static MessagingInputData forSendMessage(String channelId, String senderId, String messageText) {
        return new MessagingInputData(null, null, null, false,
                channelId, senderId, messageText, 0);
    }

    public static MessagingInputData forRefresh(String channelId, long timestamp) {
        return new MessagingInputData(null, null, null, false,
                channelId, null, null, timestamp);
    }
    
    public UUID getRequestId() {
        return requestId;
    }
    
    public UUID getOfferId() {
        return offerId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public boolean isRequest() {
        return isRequest;
    }
    
    public String getChannelId() {
        return channelId;
    }
    
    public String getSenderId() {
        return senderId;
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    public long getLastTimestamp() {
        return lastTimestamp;
    }
}
