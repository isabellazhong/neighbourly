// java
package app;

import java.awt.BorderLayout;

import javax.swing.*;

import interface_adapter.login.LoginViewModel;
import view.homepage.HomepageView;
import view.start_interface.LoginView;

import use_case.offer.CreateOfferInteractor;
import interface_adapter.offer.CreateOfferController;

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
}
