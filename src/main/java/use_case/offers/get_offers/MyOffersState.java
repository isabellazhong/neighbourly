package use_case.offers.get_offers;

import entity.Offer;
import java.util.ArrayList;
import java.util.List;

public class MyOffersState {
    private List<Offer> offers = new ArrayList<>();
    private String error = null;

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
