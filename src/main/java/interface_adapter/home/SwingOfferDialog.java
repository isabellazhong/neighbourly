package interface_adapter.home;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.awt.Dialog;
import java.awt.Window;
import view.offer_interface.CreateOfferView;
import interface_adapter.offers.create_offer.CreateOfferController;

public class SwingOfferDialog implements OfferDialog {
    private final Window owner;
    private final CreateOfferController controller;

    public SwingOfferDialog(Window owner, CreateOfferController controller) {
        this.owner = owner;
        this.controller = controller;
    }

    @Override
    public void show() {
        SwingUtilities.invokeLater(() -> {
            JDialog dlg = new JDialog(owner, "Offer Help", Dialog.ModalityType.APPLICATION_MODAL);
            CreateOfferView view = new CreateOfferView();
            if (controller != null) {
                view.setCreateOfferController(controller);
            }
            dlg.setContentPane(view);
            dlg.pack();
            dlg.setLocationRelativeTo(owner);
            dlg.setVisible(true);
        });
    }
}
