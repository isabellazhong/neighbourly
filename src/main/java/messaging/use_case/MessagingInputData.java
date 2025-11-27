package messaging.use_case;

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
    
    // Constructor for openChat
    public MessagingInputData(UUID requestId, UUID offerId, String userId, boolean isRequest) {
        this.requestId = requestId;
        this.offerId = offerId;
        this.userId = userId;
        this.isRequest = isRequest;
    }
    
    // Constructor for sendMessage
    public MessagingInputData(String channelId, String senderId, String messageText) {
        this.channelId = channelId;
        this.senderId = senderId;
        this.messageText = messageText;
    }
    
    // Constructor for refreshMessages
    public MessagingInputData(String channelId, long lastTimestamp) {
        this.channelId = channelId;
        this.lastTimestamp = lastTimestamp;
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
