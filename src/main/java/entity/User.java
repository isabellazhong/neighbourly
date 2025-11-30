package entity;

import java.util.*;

/**
 * Class for a user of our app.
 * 
 * @param name     is the name of the user
 * @param lastName is the last name of the user
 * @param email    is the email of the user
 * @param gender   is the gender of the user
 * @param requests are a map of requests of the user - mapping request id to
 *                 request
 * @param offers   are a map of the offers from the user - mapping offer id to
 *                 offer
 * @param id       is the id of the user
 */
public class User {
    private String name;
    private String lastName;
    private String email;
    private String gender;
    private List<UUID> requests;
    private List<UUID> offers;
    private UUID id;
    private Address address;
    private String password;

    // defualt constructor for mongodb
    public User() {
        this.name = "";
        this.lastName = "";
        this.email = "";
        this.gender = "";
        this.requests = new ArrayList<>();
        this.offers = new ArrayList<>();
        this.id = null;
        this.address = null;
        this.password = "";
    }

    public User(String name,
            String lastName,
            String email,
            Gender gender,
            List<UUID> requests,
            List<UUID> offers,
            UUID id,
            Address address,
            String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.requests = requests;
        this.offers = offers;
        this.gender = gender.getLabel();
        this.id = java.util.UUID.randomUUID();
        this.address = address;
        this.password = password;
    }

    public void addRequest(UUID id) {
        this.requests.add(id);
    }

    public void addOffer(UUID id) {
        this.offers.add(id);
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

    public List<UUID> getRequestIDs() {
        return this.requests;
    }

    public List<UUID> getOfferIDs() {
        return this.offers;
    }

    public UUID getID() {
        return this.id;
    }

    public Address getAddress() {
        return this.address;
    }

    public String getPassword() {
        // change later to store hashed password instead
        return this.password;
    }

    // Setter methods for MongoDB deserialization
    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setRequests(List<UUID> requests) {
        this.requests = requests;
    }

    public void setOffers(List<UUID> offers) {
        this.offers = offers;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
