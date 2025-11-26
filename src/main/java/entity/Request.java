package entity;
import java.util.UUID;
import java.util.Date;


public class Request {
    private UUID id;
    private String title;
    private String details;
    private boolean service; //true = service, false = recource
    private byte[] imagePath;
    private boolean fulfilled;
    private Date expirationDate;

    public Request(String title, String details, Boolean service, Date expirationDate) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.details = details;
        this.service = service;
        this.expirationDate = expirationDate;
        this.fulfilled = false;
        this.imagePath = null;

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
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public boolean isService() {
        return service;
    }
    public void setService(boolean service) {
        this.service = service;
    }
    public byte[] getImagePath() {
        return imagePath;
    }
    public void setImagePath(byte[] imagePath) {
        this.imagePath = imagePath;
    }
    public boolean isFulfilled() {
        return fulfilled;
    }
    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
    public Date getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
