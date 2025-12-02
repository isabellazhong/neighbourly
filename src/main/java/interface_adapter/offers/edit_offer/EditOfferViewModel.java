package interface_adapter.offers.edit_offer;

import interface_adapter.ViewModel;
import use_case.offers.edit_offers.EditOfferState;

public class EditOfferViewModel extends ViewModel<EditOfferState> {
    public EditOfferViewModel() {
        super("edit offer");
        setState(new EditOfferState());
    }
}