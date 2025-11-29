package database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import entity.*;
import java.util.UUID; 

public class MongoDB {
    private String uri; 
    private String databaseName; 
    private String collectionName;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDB() {
        uri = System.getenv("mongodbURI");
        databaseName = "Neighbourly";
        collectionName = "Users";
        
        if (uri == null || uri.isEmpty()) {
            System.err.println("Warning: mongodbURI environment variable not set. MongoDB operations will fail.");
            mongoClient = null;
            database = null;
            collection = null;
        } else {
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(databaseName);
            collection = database.getCollection(collectionName);
        }
    }

    public void addUser(User user) {
        if (collection == null) {
            System.err.println("Error: MongoDB connection not initialized");
            return;
        }
        Document userDocument = new Document("id", user.getID())
                .append("first_name", user.getName())
                .append("last_name", user.getLastName())
                .append("email", user.getLastName())
                .append("gender", user.getGender())
                .append("request_id", user.getAllRequestIDs()); 
        collection.insertOne(userDocument);
    }

    public void addOffer(Offer offer) {
        if (database == null) {
            System.err.println("Error: MongoDB connection not initialized");
            return;
        }
        Document offerDocument = new Document("id", offer.getId().toString())
                .append("title", offer.getTitle())
                .append("details", offer.getAlternativeDetails())
                .append("postDate", offer.getPostDate())
                .append("accepted", offer.isAccepted());
        MongoCollection<Document> offerscollection = database.getCollection("Offers");
        offerscollection.insertOne(offerDocument);
    }

    public void addRequest(Request request) {
        //TO implement 
    }

    public User getUserByEmail(String email) {
        if (collection == null) {
            System.err.println("Error: MongoDB connection not initialized");
            return null;
        }
        Document userDoc = collection.find(Filters.eq("email", email)).first();
        if (userDoc == null) {
            return null;
        }
        Object idObj = userDoc.get("id");
        UUID id;
        if (idObj instanceof UUID) {
            id = (UUID) idObj;
        } else if (idObj instanceof String) {
            id = UUID.fromString((String) idObj);
        } else {
            return null;
        }
        String firstName = userDoc.getString("first_name");
        String lastName = userDoc.getString("last_name");
        String gender = userDoc.getString("gender");
        return new User(id, firstName, lastName, email, gender);
    }

    public boolean updateUser(User user) {
        if (collection == null) {
            return false;
        }
        try {
            collection.updateOne(
                Filters.eq("id", user.getID()),
                Updates.combine(
                    Updates.set("first_name", user.getName()),
                    Updates.set("last_name", user.getLastName()),
                    Updates.set("email", user.getEmail()),
                    Updates.set("gender", user.getGender())
                )
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
