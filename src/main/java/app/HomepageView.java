package main.java.app;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomepageView extends JPanel {
    public HomepageView() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setLayout(new BorderLayout());
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(24, 24, 24, 24));
        content.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Large search bar
        JTextField searchField = new JTextField();
        searchField.setFont(searchField.getFont().deriveFont(28f));
        searchField.setColumns(40);
        searchField.setPreferredSize(new Dimension(1000, 72));

        JButton searchButton = new JButton("Search Requests");
        searchButton.setFont(searchButton.getFont().deriveFont(18f));

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setOpaque(false);
        row.add(searchField, BorderLayout.CENTER);
        row.add(searchButton, BorderLayout.EAST);

        // Control vertical position by adjusting topWeight
        double topWeight = 0.2;
        double bottomWeight = Math.max(0.0, 1.0 - topWeight);

        // Top filler (absorbs extra vertical space)
        gbc.gridy = 0;
        gbc.weighty = topWeight;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel topFiller = new JPanel();
        topFiller.setOpaque(false);
        topFiller.setOpaque(false);
        content.add(topFiller, gbc);

        // The search row (no vertical weight so it stays at preferred height)
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        content.add(row, gbc);

        // Bottom filler (absorbs remaining extra vertical space)
        gbc.gridy = 2;
        gbc.weighty = bottomWeight;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel bottomFiller = new JPanel();
        bottomFiller.setOpaque(false);
        content.add(bottomFiller, gbc);

        add(content, BorderLayout.CENTER);
        setPreferredSize(new Dimension(1200, 320));
    }
}
