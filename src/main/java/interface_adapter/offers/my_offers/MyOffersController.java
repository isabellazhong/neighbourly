package interface_adapter.offers.my_offers;

import use_case.offers.get_offers.MyOffersInputBoundary;

public class MyOffersController {
    final MyOffersInputBoundary myOffersInteractor;

    public MyOffersController(MyOffersInputBoundary myOffersInteractor) {
        this.myOffersInteractor = myOffersInteractor;
    }

    public void execute() {
        myOffersInteractor.execute();
    }
}
