// java
package main.java.app;

import javax.swing.*;
import java.awt.*;


public class AppBuilder {
    private static CardLayout cardLayout;
    private static JPanel cards;

    public static final String CARD_LOGIN = "login";
    public static final String CARD_HOME = "home";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(AppBuilder::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Neighbourly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        HomepageView homepageView = new HomepageView();

        cards.add(homepageView, CARD_HOME);

        cardLayout.show(cards, CARD_LOGIN); // start with login

        frame.add(cards, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void showCard(String name) {
        if (cardLayout != null && cards != null) {
            cardLayout.show(cards, name);
        }
    }
}
