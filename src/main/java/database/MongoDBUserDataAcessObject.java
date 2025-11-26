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
                boolean validPassword = userDoc.getPassword().equals(password);
                if (!validPassword) {
                    throw new IncorrectPasswordException("Incorrect Password.");
                }
            } else {
                throw new UserNotFoundException("UserNotFound.");
            }
            return userDoc;
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
