// java
package main.java.app;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

import main.java.interface_adapter.login.LoginController;
import main.java.interface_adapter.login.LoginViewModel;
import main.java.use_case.login.LoginInteractor;
import main.java.view.start_interface.LoginView;

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

        Runnable onLoginSuccess = () -> SwingUtilities.invokeLater(() -> {
            frame.getContentPane().removeAll();
            HomepageView homepageView = new HomepageView();
            frame.add(homepageView, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        });

        LoginView loginView = new LoginView(new LoginViewModel());
        frame.add(loginView, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
