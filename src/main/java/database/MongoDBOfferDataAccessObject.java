package database;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import entity.Offer;
import use_case.offers.create_offer.OfferDataAccessInterface;

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
    public List<Offer> allOffers() {
        List<Offer> offers = new ArrayList<>();
        FindIterable<Document> iterable = offersCollection.find();

        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                offers.add(documentToOffer(doc));
            }
        }
        return offers;
    }

    @Override
    public List<Offer> myOffers(String userIDString) {
        return allOffers();
    }

    @Override
    public Offer getOfferByID(java.util.UUID offerId) {
        Document doc = offersCollection.find(Filters.eq("id", offerId.toString())).first();
        if (doc != null) {
            return documentToOffer(doc);
        }
        return null;
    }

    @Override
    public void updateOffer(Offer offer) {
        offersCollection.updateOne(Filters.eq("id",
                offer.getId().toString()), Updates.combine(Updates.set("title", offer.getTitle()),
                Updates.set("details", offer.getAlternativeDetails())));
    }

    private Offer documentToOffer(Document doc) {
        String title = doc.getString("title");
        String details = doc.getString("details");
        Date date = doc.getDate("postDate");
        String idStr = doc.getString("id");
        Offer offer = new Offer(title, details, date);
        if (idStr != null) {
            offer.setId(UUID.fromString(idStr));
        }
        return offer;
    }

    public String getChatChannelId(UUID offerId) {
        Document offerDoc = offersCollection.find(Filters.eq("id", offerId.toString())).first();
        if (offerDoc != null && offerDoc.containsKey("chatChannelId")) {
            return offerDoc.getString("chatChannelId");
        }
        return null;
    }

    public void setChatChannelId(UUID offerId, String chatChannelId) {
        offersCollection.updateOne(
            Filters.eq("id", offerId.toString()),
            Updates.set("chatChannelId", chatChannelId)
        );
    }

    public void setAccepted(UUID offerId, boolean accepted) {
        offersCollection.updateOne(
            Filters.eq("id", offerId.toString()),
            Updates.set("accepted", accepted)
        );
    }
}


