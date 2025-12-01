package interface_adapter.offers.my_offers;

import interface_adapter.ViewModel;
import use_case.offers.get_offers.MyOffersState;

public class MyOffersViewModel extends ViewModel<MyOffersState> {
    public MyOffersViewModel() {
        super("my_offers");
        setState(new MyOffersState());
    }
}
