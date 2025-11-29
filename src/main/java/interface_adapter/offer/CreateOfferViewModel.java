package interface_adapter.offer;

import interface_adapter.ViewModel;
import use_case.offer.CreateOfferState;

public class CreateOfferViewModel extends ViewModel<CreateOfferState> {
    public CreateOfferViewModel() {
        super("create offer");
        setState(new CreateOfferState());
    }
}
