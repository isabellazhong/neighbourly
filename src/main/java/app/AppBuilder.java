// java
package main.java.app;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;
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

        LoginView loginView = new LoginView();
        frame.add(loginView, BorderLayout.CENTER); 

        frame.pack();
        frame.setVisible(true);
    }
}
