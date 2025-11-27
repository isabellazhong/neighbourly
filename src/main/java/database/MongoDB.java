package database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase; 

public class MongoDB {
    private String uri; 
    private String databaseName; 
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDB() {
        uri = System.getenv("mongodbURI");
        if (uri == null || uri.isEmpty()) {
            uri = "mongodb://localhost:27017"; // Default fallback
        }
        databaseName = "Neighbourly";
        
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase(databaseName);
    }

    public MongoDatabase getDatabase() {
        return this.database; 
    }

    public MongoClient getMongoClient() {
        return this.mongoClient; 
    }
    
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
