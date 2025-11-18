package main.java.domain;

import java.util.Date;

public class HelpOffer {

    private int id;
    private String title;
    private String alternativeDetails;
    private byte[] imagePath;
    private boolean accepted;
    private Date postDate;

    public HelpOffer(int id, String title, String details, Date postDate) {
        this.id = id;
        this.title = title;
        this.alternativeDetails = details;
        this.postDate = postDate;
        this.accepted = false;
        this.imagePath = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}