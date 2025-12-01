package view.offer_interface;

import entity.Offer;
import interface_adapter.offers.my_offers.MyOffersViewModel;
import view.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyOffersView extends JPanel {
    public static final String FONT_NAME = "SansSerif";
    private final String viewName = "my_offers";
    private final MyOffersViewModel myOffersViewModel;

    private static final int PANEL_WIDTH = 720;
    private static final int PANEL_HEIGHT = 460;

    private JLabel pageTitleLabel;
    private JPanel offersListPanel;
    private JScrollPane scrollPane;

    public MyOffersView(MyOffersViewModel myOffersViewModel) {
        this.myOffersViewModel = myOffersViewModel;
        initializeComponents();
        setupLayout();
        setupStyling();
    }

    private void initializeComponents() {
        pageTitleLabel = new JLabel("My Offers", SwingConstants.CENTER);
        pageTitleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 30));
        pageTitleLabel.setForeground(UIConstants.darkGray);

        offersListPanel = new JPanel();
        offersListPanel.setLayout(new BoxLayout(offersListPanel, BoxLayout.Y_AXIS));
        offersListPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(offersListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.textColorFaded, 1));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 40, 25, 40));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(pageTitleLabel);
        titlePanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupStyling() {
        setBackground(UIConstants.textFieldBackground);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    public void showOffers(List<Offer> offers) {
        offersListPanel.removeAll();

        if (offers == null || offers.isEmpty()) {
            JLabel emptyLabel = new JLabel("No offers found.");
            emptyLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
            emptyLabel.setForeground(UIConstants.textColorFaded);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            offersListPanel.add(Box.createVerticalStrut(20));
            offersListPanel.add(emptyLabel);
        } else {
            for (Offer offer : offers) {
                offersListPanel.add(Box.createVerticalStrut(10));

                JPanel rowPanel = new JPanel(new BorderLayout());
                rowPanel.setBackground(Color.WHITE);
                rowPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Fix height

                JLabel titleLabel = new JLabel(offer.getTitle());
                titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 16));
                titleLabel.setForeground(UIConstants.darkGray);

                JLabel detailsLabel = new JLabel(offer.getAlternativeDetails());
                detailsLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
                detailsLabel.setForeground(Color.GRAY);

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                String dateStr = sdf.format(offer.getPostDate());
                JLabel dateLabel = new JLabel(dateStr);
                dateLabel.setFont(new Font(FONT_NAME, Font.ITALIC, 12));
                dateLabel.setForeground(new Color(150, 150, 150));

                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setOpaque(false);
                textPanel.add(titleLabel);
                textPanel.add(Box.createVerticalStrut(5));
                textPanel.add(detailsLabel);
                textPanel.add(Box.createVerticalStrut(10));
                textPanel.add(dateLabel);

                JButton editButton = new JButton("Edit");
                editButton.setFocusPainted(false);

                rowPanel.add(textPanel, BorderLayout.CENTER);
                rowPanel.add(editButton, BorderLayout.EAST);

                offersListPanel.add(rowPanel);
            }
        }

        offersListPanel.revalidate();
        offersListPanel.repaint();
    }

    public String getViewName() {
        return viewName;
    }
}