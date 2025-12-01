package view.homepage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import interface_adapter.offers.create_offer.CreateOfferController;
import interface_adapter.offers.my_offers.MyOffersViewModel;
import interface_adapter.offers.my_offers.MyOffersController;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileViewModel;
import view.offer_interface.CreateOfferView;
import view.map.RequestLocation;
import view.map.RequestMapView;
import view.offer_interface.MyOffersView;
import view.profile_interface.ProfileView;
import java.awt.event.ActionListener;

public class HomepageView extends JPanel {
    private final CreateOfferController createOfferController;
    private final MyOffersController myOffersController;
    private final MyOffersViewModel myOffersViewModel;
    private String mapboxToken;
    private ProfileController profileController;
    private ProfileViewModel profileViewModel;
    private String viewName;

    public HomepageView(CreateOfferController createOfferController,
                        MyOffersController myOffersController,
                        MyOffersViewModel myOffersViewModel) {
        this.viewName = "homepage";
        this.createOfferController = createOfferController;
        this.myOffersController = myOffersController;
        this.myOffersViewModel = myOffersViewModel;
        // Try to get mapbox token from environment or system property
        this.mapboxToken = resolveMapboxToken();
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton myOffersButton = new JButton("My Offers");
        myOffersButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        myOffersButton.addActionListener(e -> openOffers());

        JButton profileButton = new JButton("Profile");
        profileButton.setFont(profileButton.getFont().deriveFont(14f));
        profileButton.addActionListener(e -> openProfile());

        topPanel.add(myOffersButton);
        topPanel.add(profileButton);
        add(topPanel, BorderLayout.NORTH);

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

        JButton createButton = new JButton("+");
        createButton.setFont(createButton.getFont().deriveFont(Font.PLAIN, 28f));
        createButton.setPreferredSize(new Dimension(64, 64));
        createButton.setFocusPainted(false);
        createButton.setBackground(new Color(0, 153, 76)); // green accent
        createButton.setForeground(Color.WHITE);
        createButton.setBorder(BorderFactory.createEmptyBorder());
        createButton.setOpaque(true);
        createButton.setBorderPainted(false);
        createButton.setToolTipText("Create");

        //create menu to choose between what to create when button is clicked
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu menu = new JPopupMenu();
                JMenuItem requestItem = new JMenuItem("Create New Request");
                JMenuItem offerItem = new JMenuItem("Offer Help");

                Font menuFont = new Font("SansSerif", Font.PLAIN, 14);
                requestItem.setFont(menuFont);
                offerItem.setFont(menuFont);

                requestItem.addActionListener(evt -> {
                    Window owner = SwingUtilities.getWindowAncestor(HomepageView.this);
                    CreateRequest dialog = new CreateRequest(owner);
                    dialog.setVisible(true);
                });

                offerItem.addActionListener(evt -> {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(HomepageView.this);
                    JDialog popup = new JDialog(topFrame, "Offer Help", true);

                    CreateOfferView offerView = new CreateOfferView();
                    offerView.setCreateOfferController(createOfferController);

                    popup.setContentPane(offerView);
                    popup.pack();
                    popup.setLocationRelativeTo(topFrame);
                    popup.setVisible(true);
                });

                menu.add(requestItem);
                menu.add(offerItem);
                int yOffset = -menu.getPreferredSize().height;
                menu.show(createButton, 0, yOffset);
            }
        });

        createButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        createButton.setMargin(new Insets(0, 0, 0, 0));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        bottomPanel.setOpaque(false);
        bottomPanel.add(createButton);
        add(bottomPanel, BorderLayout.SOUTH);


        setPreferredSize(new Dimension(1200, 700));
    }

    private String resolveMapboxToken() {
        String token = System.getenv("MAPBOX_TOKEN");
        if (token == null || token.isBlank()) {
            token = System.getProperty("MAPBOX_TOKEN");
        }
        return token;
    }

    private JPanel buildRequestsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("Nearby requests");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(header);
        panel.add(Box.createVerticalStrut(8));

        List<RequestLocation> demoRequests = List.of(
                new RequestLocation("REQ-001", "Grocery drop-off (Toronto)", 43.6532, -79.3832, 43.6617, -79.3950),
                new RequestLocation("REQ-002", "Medication pickup (Toronto)", 43.7000, -79.4000, 43.7100, -79.4200)
        );

        for (RequestLocation location : demoRequests) {
            panel.add(createRequestRow(location));
            panel.add(Box.createVerticalStrut(6));
        }

        if (mapboxToken == null || mapboxToken.isBlank()) {
            JLabel warning = new JLabel("Set MAPBOX_TOKEN env/system property to enable maps + ETA");
            warning.setForeground(Color.RED.darker());
            warning.setFont(warning.getFont().deriveFont(Font.PLAIN, 12f));
            panel.add(Box.createVerticalStrut(6));
            panel.add(warning);
        }

        return panel;
    }

    private JPanel createRequestRow(RequestLocation location) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(6, 8, 6, 8)
        ));

        JLabel title = new JLabel(location.title());
        row.add(title, BorderLayout.CENTER);

        JButton accept = new JButton("Accept & view map");
        accept.addActionListener(evt -> openRequestOnMap(location));
        row.add(accept, BorderLayout.EAST);

        return row;
    }

    private void openRequestOnMap(RequestLocation location) {
        if (mapboxToken == null || mapboxToken.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Mapbox token not set. Add MAPBOX_TOKEN as an environment variable or JVM property.",
                    "Map unavailable",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Request location", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setContentPane(new RequestMapView(mapboxToken, location));
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to open map: " + ex.getMessage(),
                    "Map error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void setProfileViewModel(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
    }

    private void openProfile() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null && profileController != null && profileViewModel != null) {
            JDialog profileDialog = new JDialog(topFrame, "Profile", true);
            ProfileView profileView = new ProfileView(profileViewModel);
            profileView.setProfileController(profileController);
            profileDialog.setContentPane(profileView);
            profileDialog.pack();
            profileDialog.setLocationRelativeTo(topFrame);
            profileDialog.setVisible(true);
        }
    }

    private void openOffers() {
        if (myOffersController != null) {
            myOffersController.execute();
        }
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(topFrame, "My Offers", true);

        MyOffersView myOffersView = new MyOffersView(myOffersViewModel);

        if (myOffersViewModel.getState() != null) {
            myOffersView.showOffers(myOffersViewModel.getState().getOffers());
        }

        dialog.setContentPane(myOffersView);
        dialog.setSize(720, 500);
        dialog.setLocationRelativeTo(topFrame);
        dialog.setVisible(true);
    }

    public class CreateRequest extends JDialog {
        public CreateRequest(Window owner) {
            super(owner, "Request", ModalityType.APPLICATION_MODAL);
            initUI();
        }

        private void initUI() {
            JPanel root = new JPanel(new BorderLayout(12, 12));
            root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            // REQUEST HEADER
            JLabel header = new JLabel("Create a Request", SwingConstants.LEFT);
            header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
            root.add(header, BorderLayout.NORTH);

            // Content
            JPanel column = new JPanel();
            column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
            column.setOpaque(false);


            // Title label
            JLabel titleLabel = new JLabel("Title");
            titleLabel.setFont(titleLabel.getFont().deriveFont(16f));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            column.add(titleLabel);

            JTextField titleField = new JTextField();
            titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26)); // slim height
            titleField.setPreferredSize(new Dimension(400, 26));
            titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
            titleField.setToolTipText("Short title");
            column.add(titleField);
            column.add(Box.createVerticalStrut(10));

            // Header for request type
            JLabel sectionHeading = new JLabel("Type");
            sectionHeading.setFont(sectionHeading.getFont().deriveFont(16f));
            sectionHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
            sectionHeading.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            column.add(sectionHeading);


            // Request types (Service / Resource)
            JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            optionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JRadioButton serviceBtn = new JRadioButton("Service");
            JRadioButton resourceBtn = new JRadioButton("Resource");
            ButtonGroup bg = new ButtonGroup();
            bg.add(serviceBtn);
            bg.add(resourceBtn);
            serviceBtn.setSelected(true);
            optionPanel.add(serviceBtn);
            optionPanel.add(resourceBtn);
            column.add(optionPanel);
            column.add(Box.createVerticalStrut(12));

            // Additional details heading + larger text area
            JLabel detailsLabel = new JLabel("Additional details");
            detailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            column.add(detailsLabel);

            JTextArea detailsArea = new JTextArea(8, 40);
            detailsArea.setLineWrap(true);
            detailsArea.setWrapStyleWord(true);
            JScrollPane detailsScroll = new JScrollPane(detailsArea);
            detailsScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            column.add(detailsScroll);

            root.add(column, BorderLayout.CENTER);


            // Buttons
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton cancel = new JButton("Back");
            cancel.addActionListener((ActionEvent e) -> dispose());
            JButton create = new JButton("Post Request");
            create.addActionListener((ActionEvent e) -> {
                dispose();
            });
            buttons.add(cancel);
            buttons.add(create);

            root.add(buttons, BorderLayout.SOUTH);

            setContentPane(root);
            setPreferredSize(new Dimension(720, 460));
            pack();
            setResizable(false);
            setLocationRelativeTo(getOwner());
        }
    }

    public String getViewName() {
        return this.viewName; 
    }
}
