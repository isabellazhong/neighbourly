package entity;

public class Address {
    private String street;
    private String city; 
    private String region; 
    private String postalCode; 
    private String country; 

    // default constructor for mongodb use case
    public Address() {
        street = "";
        city = "";
        region = "";
        postalCode = "";
        country = ""; 
    }

    public Address(String street,
        String city,
        String region,
        String postalCode,
        String country
    ) {
        this.street = street; 
        this.city = city; 
        this.region = region;
        this.postalCode = postalCode; 
        this.country = country; 
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city; 
    }

    public String getRegion() {
        return region;
    }

    public String getPostalCode() {
        return postalCode; 
    }

    public String getCountry() {
        return country; 
    }
}
