package view.homepage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import interface_adapter.offer.CreateOfferController;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileViewModel;
import view.offer_interface.CreateOfferView;
import view.profile_interface.ProfileView;
import java.awt.event.ActionListener;

public class HomepageView extends JPanel {
    private final CreateOfferController createOfferController;
    private ProfileController profileController;
    private ProfileViewModel profileViewModel;
    private String viewName;

    public HomepageView(CreateOfferController createOfferController) {
        this.viewName = "homepage";
        this.createOfferController = createOfferController;
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton profileButton = new JButton("Profile");
        profileButton.setFont(profileButton.getFont().deriveFont(14f));
        profileButton.addActionListener(e -> openProfile());
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
