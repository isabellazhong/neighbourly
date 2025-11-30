package interface_adapter.offers.my_offers;

import entity.Offer;
import use_case.offers.get_offers.MyOffersOutputBoundary;
import use_case.offers.get_offers.MyOffersState;

import java.util.List;

public class MyOffersPresenter implements MyOffersOutputBoundary {
    private final MyOffersViewModel myOffersViewModel;

    public MyOffersPresenter(MyOffersViewModel myOffersViewModel) {
        this.myOffersViewModel = myOffersViewModel;
    }

    @Override
    public void presentMyOffers(List<Offer> offers) {
        MyOffersState state = myOffersViewModel.getState();
        state.setOffers(offers);
        state.setError(null);

        myOffersViewModel.setState(state);
        myOffersViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        MyOffersState state = myOffersViewModel.getState();
        state.setError(error);
        myOffersViewModel.setState(state);
        myOffersViewModel.firePropertyChange();
    }
}
