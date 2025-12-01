package entity;

public class MessageDTO {
    private String messageId;
    private String userId;
    private String message;
    private long timestamp;

    public MessageDTO(String messageId, String userId, String message, long timestamp) {
        this.messageId = messageId;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

