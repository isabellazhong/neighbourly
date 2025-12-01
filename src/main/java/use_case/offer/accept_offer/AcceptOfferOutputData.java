package use_case.offer.accept_offer;

public class AcceptOfferOutputData {
    private final String channelId;
    private final String accepterUserId;

    public AcceptOfferOutputData(String channelId, String accepterUserId) {
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
