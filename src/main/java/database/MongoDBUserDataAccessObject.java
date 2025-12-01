package database;

import database.exceptions.FetchingErrorException;
import database.exceptions.IncorrectPasswordException;
import database.exceptions.UserNotFoundException;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.MongoTimeoutException;
import static com.mongodb.client.model.Filters.eq;

import entity.*;
import org.bson.types.Binary;
import use_case.start.UserDataAccessInterface;
import org.bson.Document;

import java.util.UUID;
import java.util.logging.Logger;

public class MongoDBUserDataAccessObject extends MongoDB implements UserDataAccessInterface {
    private final MongoCollection<Document> collection;
    // constants for mongodb extraction of user values
    static final String NAME = "name";
    static final String LAST_NAME = "lastName";
    static final String EMAIL = "email";
    static final String GENDER = "gender";
    static final String PASSWORD = "password";
    static final String ID = "id";
    static final Logger logger = Logger.getLogger(MongoDBUserDataAccessObject.class.getName());

    public MongoDBUserDataAccessObject() {
        super();
        String collectionName = "Users";
        collection = this.getDatabase().getCollection(collectionName);
    }

    public boolean checkExistingUser(String email) {
        return collection.find(eq(NAME, email)).first() != null;
    }

    public void addUser(User user) {
        Document userDoc = new Document()
                .append(NAME, user.getName())
                .append(LAST_NAME, user.getLastName())
                .append(EMAIL, user.getEmail())
                .append(GENDER, user.getGender())
                .append(PASSWORD, user.getPassword())
                .append(ID, user.getID());

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
        logger.info("User " + user.getName() + " has been added to the collection");
    }

    public User getUser(String email, String password) throws Exception {
        try {
            Document userDoc = collection.find(eq(EMAIL, email)).first();
            if (userDoc != null) {
                String name = userDoc.getString(NAME);
                String lastName = userDoc.getString(LAST_NAME);
                String userEmail = userDoc.getString(EMAIL);
                String gender = userDoc.getString(GENDER);
                String userPassword = userDoc.getString(PASSWORD);

                boolean validPassword = userPassword.equals(password);
                if (!validPassword) {
                    throw new IncorrectPasswordException("Incorrect Password.");
                }

                UUID userId = null;
                Object idField = userDoc.get(ID);
                if (idField instanceof Binary binaryId) {
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
                user.setAddress(address);

                return user;
            } else {
                throw new UserNotFoundException("UserNotFound.");
            }
        } catch (MongoTimeoutException e) {
            throw new UserNotFoundException("User does not exist.");
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            throw e;
        } catch (Exception e) {
            throw new FetchingErrorException("Unable to fetch user: " + e.getMessage());
        }
    }

    public boolean updateUser(User user) {
        collection.updateMany(
                Filters.eq(ID, user.getID()),
                Updates.combine(
                        Updates.set(NAME, user.getName()),
                        Updates.set(LAST_NAME, user.getLastName()),
                        Updates.set(GENDER, user.getGender())));
        return true;
    }
}
