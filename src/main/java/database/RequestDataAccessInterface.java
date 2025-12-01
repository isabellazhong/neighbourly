package database;

import java.util.UUID;

public interface RequestDataAccessInterface {

    String getChatChannelId(UUID requestId);

    void setChatChannelId(UUID requestId, String channelId);

    void setFulfilled(UUID requestId, boolean fulfilled);

}

