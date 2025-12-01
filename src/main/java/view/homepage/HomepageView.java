package view.homepage;

import interface_adapter.home.HomepageController;
import interface_adapter.home.HomepageViewModel;
import view.request_interface.CreateRequestView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import interface_adapter.offers.create_offer.CreateOfferController;
import interface_adapter.offers.my_offers.MyOffersViewModel;
import interface_adapter.offers.my_offers.MyOffersController;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileViewModel;
import view.offer_interface.CreateOfferView;
import view.offer_interface.MyOffersView;
import view.profile_interface.ProfileView;
import java.awt.event.ActionListener;

public class HomepageView extends JPanel {
    private final CreateOfferController createOfferController;
    private final MyOffersController myOffersController;
    private final MyOffersViewModel myOffersViewModel;

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

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

    private void initialize() {
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
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;

        // Top filler
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        JPanel topFiller = new JPanel();
        topFiller.setOpaque(false);
        content.add(topFiller, gbc);

        // Search row
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        JTextField searchField = new JTextField();
        searchField.setFont(searchField.getFont().deriveFont(28f));
        searchField.setPreferredSize(new Dimension(1000, 72));
        row.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("Search Requests");
        searchButton.setFont(searchButton.getFont().deriveFont(18f));
        row.add(searchButton, BorderLayout.EAST);

        content.add(row, gbc);

        // Bottom filler
        gbc.gridy = 2;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel bottomFiller = new JPanel();
        bottomFiller.setOpaque(false);
        content.add(bottomFiller, gbc);

        return content;
    }

    private JComponent buildBottomBar() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        bottomPanel.setOpaque(false);

        JButton createBtn = new JButton("+");
        createBtn.setFont(createBtn.getFont().deriveFont(Font.PLAIN, 28f));
        createBtn.setPreferredSize(new Dimension(64, 64));
        createBtn.setFocusPainted(false);
        createBtn.setBackground(new Color(0, 153, 76));
        createBtn.setForeground(Color.WHITE);
        createBtn.setBorder(BorderFactory.createEmptyBorder());
        createBtn.setOpaque(true);
        createBtn.setToolTipText("Create");
        createBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        createBtn.setMargin(new Insets(0, 0, 0, 0));

        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu menu = new JPopupMenu();
                JMenuItem requestItem = new JMenuItem("Create New Request");
                JMenuItem offerItem = new JMenuItem("Offer Help");

                Font menuFont = new Font("SansSerif", Font.PLAIN, 14);
                requestItem.setFont(menuFont);
                offerItem.setFont(menuFont);

                requestItem.addActionListener(evt -> controller.onCreateRequestClicked());
                offerItem.addActionListener(evt -> controller.onCreateOfferClicked());

                menu.add(requestItem);
                menu.add(offerItem);

                int yOffset = -menu.getPreferredSize().height;
                menu.show(createBtn, 0, yOffset);
            }
        });

        bottomPanel.add(createBtn);
        return bottomPanel;
    }

    private void attachViewModelListener() {
        if (viewModel == null) return;

        viewModel.addPropertyChangeListener(evt -> {
            String prop = evt.getPropertyName();

            if ("requestOpen".equals(prop) && Boolean.TRUE.equals(evt.getNewValue())) {
                Window owner = SwingUtilities.getWindowAncestor(HomepageView.this);
                CreateRequestView dlg = new CreateRequestView(owner);
                dlg.setVisible(true);
                viewModel.setRequestOpen(false);
                return;
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

            if ("errorMessage".equals(prop)) {
                String msg = (String) evt.getNewValue();
                if (msg != null && !msg.trim().isEmpty() && !msg.equals(" ")) {
                    Window owner = SwingUtilities.getWindowAncestor(HomepageView.this);
                    JOptionPane.showMessageDialog(owner, msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public String getViewName() {
        return this.viewName; 
    }
}
