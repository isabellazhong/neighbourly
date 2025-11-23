package database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import entity.*; 

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
        
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
    }

    public void addUser(User user) {
        // add in offers later 
        Document userDocument = new Document("id", user.getID())
                .append("first_name", user.getName())
                .append("last_name", user.getLastName())
                .append("email", user.getLastName())
                .append("gender", user.getGender())
                .append("request_id", user.getAllRequestIDs()); 
        collection.insertOne(userDocument);
    }

    public void addOffer(Offer offer) {
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
    
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
