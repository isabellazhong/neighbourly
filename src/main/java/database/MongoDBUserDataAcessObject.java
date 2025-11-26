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
    private MongoCollection<User> collection;

    public MongoDBUserDataAcessObject() {
        super();
        String collectionName = "Users";
        collection = this.getDatabase().getCollection(collectionName, User.class);
    }

    public void addUser(User user) {
        collection.insertOne(user);
    }

    public User getUser(String email, String password) throws Exception {
        try {
            User userDoc = collection.find(eq("email", email)).first();
            if (userDoc != null) {
                boolean validPassword = userDoc.getPassword() == password;
                if (!validPassword) {
                    throw new IncorrectPasswordException("Incorrect Password.");
                }
            } else {
                throw new UserNotFoundException("UserNotFound.");
            }
            return userDoc;
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
