package use_case.offer;

import java.io.IOException;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import app.AppBuilder;
import database.MongoDBOfferDataAccessObject;
import messaging.SendbirdMessagingService;

public class AcceptOfferInteractor {
    private final MongoDBOfferDataAccessObject offerDAO;
    private final SendbirdMessagingService messagingService;
    private final JFrame parentFrame;

    public AcceptOfferInteractor(
            MongoDBOfferDataAccessObject offerDAO,
            SendbirdMessagingService messagingService,
            JFrame parentFrame) {
        this.offerDAO = offerDAO;
        this.messagingService = messagingService;
        this.parentFrame = parentFrame;
    }

    public void execute(UUID offerId, String offererUserId, String accepterUserId) {
        try {
            // 1. Create channel between the two users
            String channelId = messagingService.createChannel(offererUserId, accepterUserId);

            // 2. Save channelId into offer entity and mark as accepted
            offerDAO.setChatChannelId(offerId, channelId);
            offerDAO.setAccepted(offerId, true);

            // 3. Persist via DAO (already done above)

            // 4. After accept completes, open MessagingView with the new channel
            SwingUtilities.invokeLater(() -> {
                AppBuilder.openMessagingView(parentFrame, channelId, accepterUserId);
            });

        } catch (IOException e) {
            String errorMsg = "Failed to create messaging channel: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(parentFrame, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            });
        } catch (Exception e) {
            String errorMsg = "Error accepting offer: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(parentFrame, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }
}

