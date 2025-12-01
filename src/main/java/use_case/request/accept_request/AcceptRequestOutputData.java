package use_case.request.accept_request;

public class AcceptRequestOutputData {
    private final String channelId;
    private final String accepterUserId;

    public AcceptRequestOutputData(String channelId, String accepterUserId) {
        this.channelId = channelId;
        this.accepterUserId = accepterUserId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getAccepterUserId() {
        return accepterUserId;
    }
}
