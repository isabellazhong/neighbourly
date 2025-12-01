package database;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import entity.Offer;
import use_case.offer.OfferDataAccessInterface;

public class MongoDBOfferDataAccessObject extends MongoDB implements OfferDataAccessInterface {
    private final MongoCollection<Document> offersCollection;

    public MongoDBOfferDataAccessObject() {
        super(); 
        String collectionName = "Offers"; 
        this.offersCollection = this.getDatabase().getCollection(collectionName);
    }

    @Override
    public void addOffer(Offer offer) {
        Document offerDocument = new Document("id", offer.getId().toString())
                .append("title", offer.getTitle())
                .append("details", offer.getAlternativeDetails())
                .append("postDate", offer.getPostDate())
                .append("accepted", offer.isAccepted())
                .append("chatChannelId", offer.getChatChannelId());
        this.offersCollection.insertOne(offerDocument);
    }

    @Override
    public String getChatChannelId(UUID offerId) {
        Document offerDoc = offersCollection.find(Filters.eq("id", offerId.toString())).first();
        if (offerDoc != null && offerDoc.containsKey("chatChannelId")) {
            return offerDoc.getString("chatChannelId");
        }
        return null;
    }

    @Override
    public void setChatChannelId(UUID offerId, String chatChannelId) {
        offersCollection.updateOne(
            Filters.eq("id", offerId.toString()),
            Updates.set("chatChannelId", chatChannelId)
        );
    }

    @Override
    public void setAccepted(UUID offerId, boolean accepted) {
        offersCollection.updateOne(
            Filters.eq("id", offerId.toString()),
            Updates.set("accepted", accepted)
        );
    }
}
