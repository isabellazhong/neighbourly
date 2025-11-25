package database;

import database.exceptions.FetchingErrorException;
import database.exceptions.IncorrectPasswordException;
import database.exceptions.UserNotFoundException;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import java.util.UUID;

import org.bson.Document;
import org.bson.types.ObjectId;

import entity.*;
import use_case.start.UserDataAccessInterface;

public class MongoDBUserDataAcessObject extends MongoDB implements UserDataAccessInterface {
    private MongoCollection<Document> collection;

    public MongoDBUserDataAcessObject() {
        super();
        String collectionName = "Users";
        collection = this.getDatabase().getCollection(collectionName);
    }

    public void addUser(User user) {
        ObjectId objectId = new ObjectId(user.getID().toString());
        Document userDocument = new Document("_id", objectId)
                .append("first_name", user.getName())
                .append("last_name", user.getLastName())
                .append("email", user.getLastName())
                .append("gender", user.getGender())
                .append("request_ids", user.getRequestIDs())
                .append("offer_ids", user.getOfferIDs())
                .append("address", user.getAddress());
        collection.insertOne(userDocument);
    }

    public User getUser(String email, String password) throws Exception {
        try {
            Document document = collection.find(eq("email", email)).first();
            if (document != null) {
                boolean validPassword = document.get("password").toString() == password;
                if (!validPassword) {
                    throw new IncorrectPasswordException("Incorrect Password.");
                }
            } else {
                throw new UserNotFoundException("UserNotFound.");
            }

            Gender gender = Gender.valueOf(document.get("gender").toString().toUpperCase());
            User user = new User(
                    document.getString("first_name"),
                    document.getString("last_name"),
                    document.getString("email"),
                    gender,
                    document.getList("request_ids", UUID.class),
                    document.getList("offer_ids", UUID.class),
                    UUID.fromString(document.getObjectId("_id").toString()),
                    document.getString("address"));
            return user;
        } catch (Exception e) {
            throw new FetchingErrorException("Unable to fetch user." + e);
        }
    }

    public void closeConnection() {
        if (this.getMongoClient() != null) {
            this.getMongoClient().close();
        }
    }
}
