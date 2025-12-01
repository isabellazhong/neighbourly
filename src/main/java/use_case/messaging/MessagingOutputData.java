package use_case.messaging;

import entity.MessageDTO;
import java.util.List;

public class MessagingOutputData {
    private List<MessageDTO> messages;
    private String channelId;
    
    public MessagingOutputData(List<MessageDTO> messages, String channelId) {
        this.messages = messages;
        this.channelId = channelId;
    }
    
    public List<MessageDTO> getMessages() {
        return messages;
    }
    
    public String getChannelId() {
        return channelId;
    }
}
