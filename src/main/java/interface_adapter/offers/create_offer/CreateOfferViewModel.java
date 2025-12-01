package interface_adapter.offers.create_offer;

import interface_adapter.ViewModel;
import use_case.offers.create_offer.CreateOfferState;

public class CreateOfferViewModel extends ViewModel<CreateOfferState> {
    public CreateOfferViewModel() {
        super("create offer");
        setState(new CreateOfferState());
    }
}
