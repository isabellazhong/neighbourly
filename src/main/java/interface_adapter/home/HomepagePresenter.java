package interface_adapter.home;

import interface_adapter.interactor.HomepageViewOutputBoundary;

public class HomepagePresenter implements HomepageViewOutputBoundary {
    private final ViewModelManager viewModelManager;
    private final HomepageViewModel tempModel;
    private OfferDialog offerDialog;

    public HomepagePresenter(ViewModelManager viewModelManager, HomepageViewModel tempModel) {
        this.viewModelManager = viewModelManager;
        this.tempModel = tempModel;
    }

    public void setOfferDialog(OfferDialog offerDialog) {
        this.offerDialog = offerDialog;
    }

    public void presentOpenRequest() { tempModel.setRequestOpen(true); }
    public void presentOpenOffer()   { tempModel.setOfferOpen(true); }
    public void presentCloseRequest(){ tempModel.setRequestOpen(false); }
    public void presentCloseOffer()  { tempModel.setOfferOpen(false); }

    public void presentError(String message) {
        tempModel.setErrorMessage(message == null || message.isEmpty() ? " " : message);
    }

    @Override
    public void prepareOfferPage() {
        tempModel.setRequestOpen(false);
        viewModelManager.refreshFrom(tempModel);

        if (offerDialog != null) {
            offerDialog.show();
            return;
        }

        tempModel.setOfferOpen(true);
        viewModelManager.refreshFrom(tempModel);
    }

    @Override
    public void prepareRequestPage() {
        tempModel.setOfferOpen(false);
        tempModel.setRequestOpen(true);
        viewModelManager.refreshFrom(tempModel);
    }

    @Override
    public void refreshHomepageState(HomepageViewModel incoming) {
        viewModelManager.refreshFrom(incoming);
    }
}
