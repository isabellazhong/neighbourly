package database;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import entity.Offer;

public class MongoDBOfferDataAccessObject extends MongoDB{
    private final MongoCollection<Document> offersCollection;

    public MongoDBOfferDataAccessObject() {
        super(); 
        String collectionName = "Offers"; 
        this.offersCollection = this.getDatabase().getCollection(collectionName);
    }

    public void addOffer(Offer offer) {
        Document offerDocument = new Document("id", offer.getId().toString())
                .append("title", offer.getTitle())
                .append("details", offer.getAlternativeDetails())
                .append("postDate", offer.getPostDate())
                .append("accepted", offer.isAccepted());
        this.offersCollection.insertOne(offerDocument);
    }
}
