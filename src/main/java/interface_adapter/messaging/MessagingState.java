package interface_adapter.messaging;

import java.util.ArrayList;
import java.util.List;

import entity.MessageDTO;

public class MessagingState {
    private String channelId = "";
    private List<MessageDTO> messages = new ArrayList<>();
    private String currentInput = "";
    private String error = null;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

