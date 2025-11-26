package entity;

public class Address {
    private final String street;
    private final String city; 
    private final String region; 
    private final String postalCode; 
    private final String country; 

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
