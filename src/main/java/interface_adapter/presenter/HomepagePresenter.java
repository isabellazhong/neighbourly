package interface_adapter.presenter;

import javax.swing.*;
import java.awt.*;

import interface_adapter.navigator.HomepageViewOutputBoundary;
import interface_adapter.home.HomepageViewModel;
import interface_adapter.home.ViewModelManager;
import view.offer_interface.CreateOfferView;

public class HomepagePresenter implements HomepageViewOutputBoundary {
    private final ViewModelManager viewModelManager;
    private final HomepageViewModel tempModel;
    private CreateOfferView offerView; // optional view instance presented directly

    public HomepagePresenter(ViewModelManager viewModelManager, HomepageViewModel tempModel) {
        this.viewModelManager = viewModelManager;
        this.tempModel = tempModel;
    }

    public void setOfferView(CreateOfferView offerView) {
        this.offerView = offerView;
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
        // Ensure requestOpen is cleared so it doesn't get propagated accidentally.
        tempModel.setRequestOpen(false);
        tempModel.setOfferOpen(true);
        viewModelManager.refreshFrom(tempModel);

        if (offerView != null) {
            SwingUtilities.invokeLater(() -> {
                JDialog dlg = new JDialog((Window) null, "Offer Help", Dialog.ModalityType.APPLICATION_MODAL);
                dlg.setContentPane(offerView);
                dlg.pack();
                dlg.setLocationRelativeTo(null);
                dlg.setVisible(true);
                // After dialog closed, clear the flag and refresh so view remains consistent.
                tempModel.setOfferOpen(false);
                viewModelManager.refreshFrom(tempModel);
            });
        }
    }

    @Override
    public void prepareRequestPage() {
        // Ensure offerOpen is cleared so it doesn't get propagated accidentally.
        tempModel.setOfferOpen(false);
        tempModel.setRequestOpen(true);
        viewModelManager.refreshFrom(tempModel);
    }

    @Override
    public void refreshHomepageState(HomepageViewModel incoming) {
        viewModelManager.refreshFrom(incoming);
    }
}
