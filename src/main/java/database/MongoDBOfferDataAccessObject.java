package database;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;

import entity.Offer;
import use_case.offers.create_offer.OfferDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class MongoDBOfferDataAccessObject extends MongoDB implements OfferDataAccessInterface {
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

    @Override
    public List<Offer> AllOffers() {
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
    public List<Offer> MyOffers(String username) {
        List<Offer> offers = new ArrayList<>();
        return AllOffers();
    }

    private Offer documentToOffer(Document doc) {
        String title = doc.getString("title");
        String details = doc.getString("details");
        Date date = doc.getDate("postDate");
        Offer offer = new Offer(title, details, date);
        return offer;
    }
}


