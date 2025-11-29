package use_case.request;

import database.MongoDBRequestDataAccessObject;
import interface_adapter.SendbirdMessagingService;
import app.AppBuilder;

import javax.swing.*;
import java.io.IOException;
import java.util.UUID;

public class AcceptRequestInteractor {
    private final MongoDBRequestDataAccessObject requestDAO;
    private final SendbirdMessagingService messagingService;
    private final JFrame parentFrame;

    public AcceptRequestInteractor(
            MongoDBRequestDataAccessObject requestDAO,
            SendbirdMessagingService messagingService,
            JFrame parentFrame) {
        this.requestDAO = requestDAO;
        this.messagingService = messagingService;
        this.parentFrame = parentFrame;
    }

    public void execute(UUID requestId, String requesterUserId, String accepterUserId) {
        try {
            // 1. Create channel between the two users
            String channelId = messagingService.createChannel(requesterUserId, accepterUserId);

            // 2. Save channelId into request entity and mark as fulfilled
            requestDAO.setChatChannelId(requestId, channelId);
            requestDAO.setFulfilled(requestId, true);

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
            String errorMsg = "Error accepting request: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(parentFrame, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }
}

