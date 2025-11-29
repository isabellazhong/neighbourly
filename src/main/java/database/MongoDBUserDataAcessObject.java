package database;

import database.exceptions.FetchingErrorException;
import database.exceptions.IncorrectPasswordException;
import database.exceptions.UserNotFoundException;

import com.mongodb.client.MongoCollection;
import com.mongodb.MongoTimeoutException;
import com.mongodb.MongoSocketException;
import static com.mongodb.client.model.Filters.eq;

import entity.*;
import use_case.start.UserDataAccessInterface;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class MongoDBUserDataAcessObject extends MongoDB implements UserDataAccessInterface {
    private MongoCollection<Document> collection;

    public MongoDBUserDataAcessObject() {
        super();
        String collectionName = "Users";
        collection = this.getDatabase().getCollection(collectionName);
    }

    public void addUser(User user) {
        Document userDoc = new Document()
                .append("name", user.getName())
                .append("lastName", user.getLastName())
                .append("email", user.getEmail())
                .append("gender", user.getGender())
                .append("password", user.getPassword())
                .append("iD", user.getID())
                .append("requestIDs", user.getRequestIDs())
                .append("offerIDs", user.getOfferIDs());

        if (user.getAddress() != null) {
            Document addressDoc = new Document()
                    .append("street", user.getAddress().getStreet())
                    .append("city", user.getAddress().getCity())
                    .append("region", user.getAddress().getRegion())
                    .append("postalCode", user.getAddress().getPostalCode())
                    .append("country", user.getAddress().getCountry());
            userDoc.append("address", addressDoc);
        }

        collection.insertOne(userDoc);
        System.out.println("Successfully inserted");
    }

    public User getUser(String email, String password) throws Exception {
        try {
            Document userDoc = collection.find(eq("email", email)).first();
            if (userDoc != null) {
                String name = userDoc.getString("name");
                String lastName = userDoc.getString("lastName");
                String userEmail = userDoc.getString("email");
                String gender = userDoc.getString("gender");
                String userPassword = userDoc.getString("password");

                System.out.println("User found - Name: '" + name + "'");
                System.out.println("User found - Password: '" + userPassword + "'");
                System.out.println("Input password: '" + password + "'");

                boolean validPassword = userPassword.equals(password);
                if (!validPassword) {
                    throw new IncorrectPasswordException("Incorrect Password.");
                }

                List<UUID> requests = new ArrayList<>();
                List<UUID> offers = new ArrayList<>();

                UUID userId = null;
                Object idField = userDoc.get("iD");
                if (idField instanceof org.bson.types.Binary) {
                    org.bson.types.Binary binaryId = (org.bson.types.Binary) idField;
                    java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(binaryId.getData());
                    long high = bb.getLong();
                    long low = bb.getLong();
                    userId = new UUID(high, low);
                }

                Address address = null;
                Document addressDoc = userDoc.get("address", Document.class);
                if (addressDoc != null) {
                    address = new Address(
                            addressDoc.getString("street"),
                            addressDoc.getString("city"),
                            addressDoc.getString("region"),
                            addressDoc.getString("postalCode"),
                            addressDoc.getString("country"));
                }

                User user = new User();
                user.setName(name);
                user.setLastName(lastName);
                user.setEmail(userEmail);
                user.setGender(gender);
                user.setPassword(userPassword);
                user.setId(userId);
                user.setRequests(requests);
                user.setOffers(offers);
                user.setAddress(address);

                return user;
            } else {
                throw new UserNotFoundException("UserNotFound.");
            }
        } catch (MongoTimeoutException e) {
            throw new UserNotFoundException("User does not exist.");
        } catch (MongoSocketException e) {
            throw new UserNotFoundException("User does not exist.");
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            throw e;
        } catch (Exception e) {
            throw new FetchingErrorException("Unable to fetch user: " + e.getMessage());
        }
    }

    public void closeConnection() {
        if (this.getMongoClient() != null) {
            this.getMongoClient().close();
        }
    }
}
