// java
package app;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import database.MongoDBOfferDataAccessObject;
import database.MongoDBRequestDataAccessObject;
import interface_adapter.login.LoginViewModel;
import interface_adapter.offer.CreateOfferController;
import messaging.MessagingView;
import messaging.SendbirdMessagingService;
import messaging.interface_adapter.MessagingController;
import messaging.interface_adapter.MessagingPresenter;
import messaging.interface_adapter.MessagingViewModel;
import messaging.use_case.MessagingInteractor;
import use_case.offer.CreateOfferInteractor;
import view.homepage.HomepageView;
import view.start_interface.LoginView;

public class AppBuilder {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Neighbourly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        CreateOfferInteractor offerInteractor = new CreateOfferInteractor(null);
        CreateOfferController offerController = new CreateOfferController(offerInteractor);

        Runnable onLoginSuccess = () -> SwingUtilities.invokeLater(() -> {
            frame.getContentPane().removeAll();
            HomepageView homepageView = new HomepageView(offerController);
            frame.add(homepageView, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
        });

        LoginView loginView = new LoginView(new LoginViewModel());
        frame.add(loginView, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Opens the MessagingView in a new dialog window.
     * This method can be called from anywhere in the application to open the messaging interface.
     * @param parentFrame The parent frame for the dialog
     * @param channelId Optional channel ID to automatically open. If null, view opens empty.
     * @param userId The current user's ID for sending messages
     */
    public static void openMessagingView(JFrame parentFrame, String channelId, String userId) {
        // Create dependencies
        MessagingViewModel messagingViewModel = new MessagingViewModel();
        MessagingPresenter messagingPresenter = new MessagingPresenter(messagingViewModel);
        SendbirdMessagingService messagingService = new SendbirdMessagingService();
        MongoDBRequestDataAccessObject requestDAO = new MongoDBRequestDataAccessObject();
        MongoDBOfferDataAccessObject offerDAO = new MongoDBOfferDataAccessObject();
        
        MessagingInteractor messagingInteractor = new MessagingInteractor(
            messagingPresenter,
            messagingService,
            requestDAO,
            offerDAO
        );
        
        MessagingController messagingController = new MessagingController(messagingInteractor);
        
        // Create view
        MessagingView messagingView = new MessagingView(messagingViewModel);
        messagingView.setMessagingController(messagingController);
        if (userId != null) {
            messagingView.setCurrentUserId(userId);
        }
        
        // If channelId is provided, open that chat
        if (channelId != null && userId != null) {
            // Fetch all messages for the channel initially
            SwingUtilities.invokeLater(() -> {
                messagingController.fetchAllMessages(channelId);
            });
        }
        
        JDialog dialog = new JDialog(parentFrame, "Messages", false);
        dialog.setContentPane(messagingView);
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
        
        // Cleanup when dialog is closed
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                messagingView.cleanup();
            }
        });
    }
    
    /**
     * Opens the MessagingView without a specific channel (for testing/manual opening).
     */
    public static void openMessagingView(JFrame parentFrame) {
        openMessagingView(parentFrame, null, null);
    }
}
