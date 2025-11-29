package entity;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Class for a user of our app.
 * @param name is the name of the user
 * @param lastName is the last name of the user
 * @param email is the email of the user
 * @param gender is the gender of the user
 * @param requests are a map of requests of the user - mapping request id to request 
 * @param offers are a map of the offers from the user - mapping offer id to offer 
 * @param id is the id of the user 
 */
public class User {
    private String name;
    private String lastName;
    private String email; 
    private String gender; 
    private Map<UUID, Request> requests;
    private Map<UUID, Offer> offers;
    private UUID id; 

    public User(String name, String lastName, String email, Gender gender) {
        this.name = name; 
        this.lastName = lastName; 
        this.email = email;
        this.requests = new HashMap<>();
        this.offers = new HashMap<>();
        this.gender = gender.getLabel(); 
        this.id = java.util.UUID.randomUUID(); 
    }

    public User(UUID id, String name, String lastName, String email, String gender) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.requests = new HashMap<>();
        this.offers = new HashMap<>();
    }

    public void addRequest(Request request) {
        this.requests.put(request.getId(), request); 
    }

    public void addOffer(Offer offer) {
        this.offers.put(offer.getId(), offer); 
    }

    public String getName() {
        return this.name;
    }

    public String getLastName() {
        return this.lastName; 
    }

    public String getEmail() {
        return this.email; 
    }

    public String getGender() {
        return this.gender;
    }

    public UUID getID() {
        return this.id; 
    }

    public UUID[] getAllRequestIDs() {
        Set<UUID> keySet = this.requests.keySet(); 
        return keySet.toArray(new UUID[0]);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
 
}
