package entity;

import java.util.*;

/**
 * Class for a user of our app.
 * @param name     is the name of the user
 * @param lastName is the last name of the user
 * @param email    is the email of the user
 * @param gender   is the gender of the user
 * @param id       is the id of the user
 * @param address is the address of the user
 * @param password is the password of the user
 */
public class User {
    private String name;
    private String lastName;
    private String email;
    private String gender;
    private UUID id;
    private Address address;
    private String password;
    private List<UUID> offers;

    // default constructor for mongodb
    public User() {
        this.name = "";
        this.lastName = "";
        this.email = "";
        this.gender = "";
        this.id = null;
        this.address = null;
        this.password = "";
        this.offers = new ArrayList<>();
    }

    public User(String name,
            String lastName,
            String email,
            Gender gender,
            UUID id,
            Address address,
            String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender.getLabel();
        this.id = java.util.UUID.randomUUID();
        this.address = address;
        this.password = password;
        this.offers = new ArrayList<>();
    }

    public User(UUID id, String name, String lastName, String email, String gender) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.offers = new ArrayList<>();
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

    public Address getAddress() {
        return this.address;
    }

    public String getPassword() {
        // change later to store hashed password instead
        return this.password;
    }

    public void addOffer(UUID id) { this.offers.add(id); }

    public List<UUID> getOfferIDs() { return this.offers; }

    public void setOfferIDs(List<UUID> offerIDs) { this.offers = offerIDs; }


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

    public void setAddress(Address address) {
        this.address = address;
    }
}
