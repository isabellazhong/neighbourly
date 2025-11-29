package entity;
import java.util.*;

public class User {
    private String name;
    private String lastName;
    private String email; 
    private String gender; 
    private List<UUID> requests;
    private List<UUID> offers;
    private UUID id; 

    public User(String name, 
        String lastName, 
        String email, 
        Gender gender, 
        List<UUID> requests, 
        List<UUID> offers,
        UUID id) 
    {
        this.name = name; 
        this.lastName = lastName; 
        this.email = email;
        this.requests = requests;
        this.offers = offers;
        this.gender = gender.getLabel(); 
        this.id = java.util.UUID.randomUUID(); 
    }

    public User(UUID id, String name, String lastName, String email, String gender) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.requests = new ArrayList<>();
        this.offers = new ArrayList<>();
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
