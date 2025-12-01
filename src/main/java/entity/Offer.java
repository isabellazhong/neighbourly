package entity;
import java.util.UUID;
import java.util.Date;

public class Offer {
    private UUID id;
    private String title;
    private String alternativeDetails;
    private byte[] imagePath;
    private boolean accepted;
    private Date postDate;
    private String chatChannelId;

    public Offer(String title, String details, Date postDate) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.alternativeDetails = details;
        this.postDate = postDate;
        this.accepted = false;
        this.imagePath = null;
        this.chatChannelId = null;
    }

    public UUID getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAlternativeDetails() {
        return alternativeDetails;
    }
    public void setAlternativeDetails(String alternativeDetails) {
        this.alternativeDetails = alternativeDetails;
    }
    public byte[] getImagePath() {
        return imagePath;
    }
    public void setImagePath(byte[] imagePath) {
        this.imagePath = imagePath;
    }
    public boolean isAccepted() {
        return accepted;
    }
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
    public Date getPostDate() {
        return postDate;
    }
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
    public String getChatChannelId() {
        return chatChannelId;
    }
    public void setChatChannelId(String chatChannelId) {
        this.chatChannelId = chatChannelId;
    }
}