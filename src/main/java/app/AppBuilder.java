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
        // Hint JavaFX to use Metal pipeline on macOS for better GPU acceleration/WebGL.
        System.setProperty("prism.order", "metal");
        System.setProperty("prism.verbose", "false");
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

        String mapboxToken = resolveMapboxToken();
        boolean skipLogin = resolveSkipLoginFlag();

        Runnable onLoginSuccess = () -> SwingUtilities.invokeLater(() -> {
            frame.getContentPane().removeAll();
            HomepageView homepageView = new HomepageView(offerController, mapboxToken);
            frame.add(homepageView, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
        });

        if (skipLogin) {
            onLoginSuccess.run();
        } else {
            LoginView loginView = new LoginView(new LoginViewModel());
            frame.add(loginView, BorderLayout.CENTER);
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static String resolveMapboxToken() {
        String token = System.getenv("MAPBOX_TOKEN");
        if (token == null || token.isBlank()) {
            token = System.getProperty("MAPBOX_TOKEN");
        }
        return token;
    }

    private static boolean resolveSkipLoginFlag() {
        String prop = System.getProperty("skipLogin");
        if (prop == null) {
            prop = System.getProperty("skiplogin"); // forgiving casing
        }
        String env = System.getenv("SKIP_LOGIN");
        return parseBooleanFlag(prop) || parseBooleanFlag(env);
    }

    private static boolean parseBooleanFlag(String value) {
        if (value == null) return false;
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("yes");
    }
}
