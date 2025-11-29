package database;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class MongoDBRequestDataAccessObject extends MongoDB implements RequestDataAccessInterface {
    private MongoCollection<Document> requestsCollection;

    public MongoDBRequestDataAccessObject() {
        super();
        String collectionName = "Requests";
        this.requestsCollection = this.getDatabase().getCollection(collectionName);
    }

    public String getChatChannelId(UUID requestId) {
        Document requestDoc = requestsCollection.find(Filters.eq("id", requestId.toString())).first();
        if (requestDoc != null && requestDoc.containsKey("chatChannelId")) {
            return requestDoc.getString("chatChannelId");
        }
        return null;
    }

    public void setChatChannelId(UUID requestId, String chatChannelId) {
        requestsCollection.updateOne(
            Filters.eq("id", requestId.toString()),
            Updates.set("chatChannelId", chatChannelId)
        );
    }

    public void setFulfilled(UUID requestId, boolean fulfilled) {
        requestsCollection.updateOne(
            Filters.eq("id", requestId.toString()),
            Updates.set("fulfilled", fulfilled)
        );
    }
}
