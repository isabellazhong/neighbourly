package view.navigator;
import javax.swing.*;
import java.awt.*;
import view.offer_interface.CreateOfferView;
import view.RequestInterface.CreateRequestView;
import interface_adapter.offer.CreateOfferController;


public class SwingPageNavigator implements PageNavigator {
    private final CreateOfferController createOfferController;

    public SwingPageNavigator(CreateOfferController createOfferController) {
        this.createOfferController = createOfferController;
    }


    @Override
    public void openCreateOffer(Window owner) {
        JDialog dialog = new JDialog(owner, "Offer Help", Dialog.ModalityType.APPLICATION_MODAL);
        CreateOfferView offerView = new CreateOfferView();
        offerView.setCreateOfferController(createOfferController);
        dialog.setContentPane(offerView);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }


    @Override
    public void openCreateRequest(Window owner) {
        CreateRequestView dialog = new CreateRequestView(owner);
        dialog.setVisible(true);
    }
}