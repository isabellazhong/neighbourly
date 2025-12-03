package view.homepage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;

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
import io.github.cdimascio.dotenv.Dotenv;
import interface_adapter.homepage.ViewModelManager;
import interface_adapter.homepage.HomepageViewModel;
import entity.Request;

public class HomepageView extends JPanel {
    private interface_adapter.homepage.HomepageController homepageController;
    private interface_adapter.homepage.ViewModelManager homepageViewModelManager;
    private final CreateOfferController createOfferController;
    private final MyOffersController myOffersController;
    private final MyOffersViewModel myOffersViewModel;
    private String mapboxToken;
    private final use_case.map.MapService mapService;
    private ProfileController profileController;
    private ProfileViewModel profileViewModel;
    private String viewName;

    // left demo requests panel (other people's requests)
    private JPanel requestsPanel;


    private final List<Request> createdRequests = new ArrayList<>();
    private JPanel createdRequestsPanel;
    private JScrollPane createdRequestsScroll;

    public void setHomepageController(interface_adapter.homepage.HomepageController controller) {
        this.homepageController = controller;
    }

    public void bindHomepageViewModelManager(interface_adapter.homepage.ViewModelManager manager) {
        this.homepageViewModelManager = manager;
        if (manager == null) return;
        manager.registerListener(new ViewModelManager.Listener() {
            @Override
            public void onViewModelUpdated(HomepageViewModel vm) {
                List<RequestLocation> nearby = vm != null ? vm.getRequests() : null;
                List<Request> created = vm != null ? vm.getCreatedRequests() : null;
                SwingUtilities.invokeLater(() -> {
                    updateRequestsPanel(nearby);
                    setCreatedRequests(created);
                });
            }
        });
    }

    public HomepageView(CreateOfferController createOfferController,
                        MyOffersController myOffersController,
                        MyOffersViewModel myOffersViewModel,
                        use_case.map.MapService mapService,
                        String mapboxToken) {
        this.viewName = "homepage";
        this.createOfferController = createOfferController;
        this.myOffersController = myOffersController;
        this.myOffersViewModel = myOffersViewModel;
        this.mapService = mapService;
        this.mapboxToken = mapboxToken != null ? mapboxToken : resolveMapboxToken();
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
        content.add(topFiller, gbc);

        // The search row (no vertical weight so it stays at preferred height)
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        content.add(row, gbc);

        // The center "My requests" scrollable area under the search
        gbc.gridy = 2;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        createdRequestsPanel = new JPanel();
        createdRequestsPanel.setLayout(new BoxLayout(createdRequestsPanel, BoxLayout.Y_AXIS));
        createdRequestsPanel.setOpaque(false);
        createdRequestsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        createdRequestsScroll = new JScrollPane(createdRequestsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        createdRequestsScroll.getVerticalScrollBar().setUnitIncrement(16);
        createdRequestsScroll.setBorder(BorderFactory.createEmptyBorder());
        content.add(createdRequestsScroll, gbc);

        // Bottom filler (absorbs remaining extra vertical space)
        gbc.gridy = 3;
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

        // Demo requests panel on the left (other people's requests)
        this.requestsPanel = new JPanel();
        this.requestsPanel.setLayout(new BoxLayout(this.requestsPanel, BoxLayout.Y_AXIS));
        this.requestsPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        this.requestsPanel.setOpaque(false);
        this.requestsPanel.setPreferredSize(new Dimension(360, 400));
        add(this.requestsPanel, BorderLayout.WEST);
        updateRequestsPanel(null); // populate left demo

        // initialize created requests area (empty)
        rebuildCreatedRequestsArea();

        setPreferredSize(new Dimension(1200, 700));
    }

    private String resolveMapboxToken() {
        String token = System.getenv("MAPBOX_TOKEN");
        if (token == null || token.isBlank()) {
            token = System.getProperty("MAPBOX_TOKEN");
        }
        if (token == null || token.isBlank()) {
            try {
                Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
                token = dotenv.get("MAPBOX_TOKEN");
            } catch (Exception ignored) {
            }
        }
        return token;
    }

    private void updateRequestsPanel(List<RequestLocation> requests) {
        requestsPanel.removeAll();
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
        requestsPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        requestsPanel.setOpaque(false);

        JLabel header = new JLabel("Nearby requests");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        requestsPanel.add(header);
        requestsPanel.add(Box.createVerticalStrut(8));

        double helperLat = 43.6617;
        double helperLng = -79.3950;
        List<RequestLocation> demoRequests = List.of(
                new RequestLocation("REQ-001", "Grocery drop-off (Toronto)", 43.6532, -79.3832, helperLat, helperLng),
                new RequestLocation("REQ-002", "Medication pickup (Toronto)", 43.7000, -79.4000, helperLat, helperLng)
        );

        List<RequestLocation> toShow = requests == null ? demoRequests : requests;

        for (RequestLocation location : toShow) {
            requestsPanel.add(createRequestRow(location));
            requestsPanel.add(Box.createVerticalStrut(6));
        }

        if (mapboxToken == null || mapboxToken.isBlank()) {
            JLabel warning = new JLabel("Set MAPBOX_TOKEN env/system property to enable maps + ETA");
            warning.setForeground(Color.RED.darker());
            warning.setFont(warning.getFont().deriveFont(Font.PLAIN, 12f));
            requestsPanel.add(Box.createVerticalStrut(6));
            requestsPanel.add(warning);
        }

        requestsPanel.revalidate();
        requestsPanel.repaint();
    }

    private void rebuildCreatedRequestsArea() {
        createdRequestsPanel.removeAll();

        JLabel header = new JLabel("My Pending requests");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        createdRequestsPanel.add(header);
        createdRequestsPanel.add(Box.createVerticalStrut(8));

        if (createdRequests.isEmpty()) {
            JLabel empty = new JLabel("You have not posted any requests yet.");
            empty.setFont(empty.getFont().deriveFont(Font.PLAIN, 13f));
            createdRequestsPanel.add(empty);
        } else {
            for (Request req : createdRequests) {
                createdRequestsPanel.add(createCreatedRequestRow(req));
                createdRequestsPanel.add(Box.createVerticalStrut(6));
            }
        }

        createdRequestsPanel.revalidate();
        createdRequestsPanel.repaint();
    }

    private JPanel createRequestRow(RequestLocation location) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(6, 8, 6, 8)
        ));
        row.setOpaque(false);

        JLabel title = new JLabel(location.title());
        row.add(title, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setOpaque(false);
        JButton accept = new JButton("Accept");
        accept.addActionListener(evt -> JOptionPane.showMessageDialog(
                this,
                "Request accepted (demo).",
                "Accepted",
                JOptionPane.INFORMATION_MESSAGE
        ));

        JButton view = new JButton("View location");
        view.addActionListener(evt -> openRequestOnMap(location));

        actions.add(accept);
        actions.add(view);
        row.add(actions, BorderLayout.EAST);

        return row;
    }

    // created requests rows now operate on entity.Request
    private JPanel createCreatedRequestRow(Request req) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(6, 8, 6, 8)
        ));
        row.setOpaque(false);

        JLabel title = new JLabel(req.getTitle());
        row.add(title, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setOpaque(false);

        JButton edit = new JButton("Edit");
        edit.addActionListener(evt -> {
            String newTitle = JOptionPane.showInputDialog(this, "Edit title:", req.getTitle());
            if (newTitle == null) return;
            newTitle = newTitle.trim();
            if (newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String newDetails = JOptionPane.showInputDialog(this, "Edit details:", req.getDetails() != null ? req.getDetails() : "");
            if (newDetails == null) return;

            if (homepageController != null) {
                homepageController.editRequest(req.getId().toString(), newTitle, newDetails, req.isService());
            } else {
                for (int i = 0; i < createdRequests.size(); i++) {
                    if (createdRequests.get(i).getId().equals(req.getId())) {
                        createdRequests.get(i).setTitle(newTitle);
                        createdRequests.get(i).setDetails(newDetails);
                        createdRequests.get(i).setService(req.isService());
                        break;
                    }
                }
                rebuildCreatedRequestsArea();
            }
        });

        JButton delete = new JButton("Delete");
        delete.addActionListener(evt -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this request?", "Confirm delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            if (homepageController != null) {
                homepageController.deleteRequest(req.getId().toString());
            } else {
                createdRequests.removeIf(r -> r.getId().equals(req.getId()));
                rebuildCreatedRequestsArea();
            }
        });

        actions.add(edit);
        actions.add(delete);
        row.add(actions, BorderLayout.EAST);

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
            dialog.setContentPane(new RequestMapView(mapService, new interface_adapter.map.RealMapImageProvider(mapboxToken), mapboxToken, location));
            dialog.pack();
            dialog.setSize(new Dimension(1300, 900));
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

    public void addCreatedRequest(Request request) {
        if (request == null) return;
        createdRequests.add(0, request);
        rebuildCreatedRequestsArea();
        if (createdRequestsScroll != null) {
            SwingUtilities.invokeLater(() -> createdRequestsScroll.getVerticalScrollBar().setValue(0));
        }
    }

    public void setCreatedRequests(List<Request> list) {
        createdRequests.clear();
        if (list != null) createdRequests.addAll(list);
        rebuildCreatedRequestsArea();
    }

    public class CreateRequest extends JDialog {
        public CreateRequest(Window owner) {
            super(owner, "Request", ModalityType.APPLICATION_MODAL);
            initUI();
        }

        private void initUI() {
            JPanel root = new JPanel(new BorderLayout(12, 12));
            root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            JLabel header = new JLabel("Create a Request", SwingConstants.LEFT);
            header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
            root.add(header, BorderLayout.NORTH);

            JPanel column = new JPanel();
            column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
            column.setOpaque(false);

            JLabel titleLabel = new JLabel("Title");
            titleLabel.setFont(titleLabel.getFont().deriveFont(16f));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            column.add(titleLabel);

            JTextField titleField = new JTextField();
            titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
            titleField.setPreferredSize(new Dimension(400, 26));
            titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
            titleField.setToolTipText("Short title");
            column.add(titleField);
            column.add(Box.createVerticalStrut(10));

            JLabel sectionHeading = new JLabel("Type");
            sectionHeading.setFont(sectionHeading.getFont().deriveFont(16f));
            sectionHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
            sectionHeading.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            column.add(sectionHeading);

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

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton cancel = new JButton("Back");
            cancel.addActionListener((ActionEvent e) -> dispose());
            JButton create = new JButton("Post Request");
            create.addActionListener((ActionEvent e) -> {
                String title = titleField.getText() != null ? titleField.getText().trim() : "";
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a title for the request", "Missing title", JOptionPane.WARNING_MESSAGE);
                    titleField.requestFocus();
                    return;
                }

                String details = detailsArea.getText() != null ? detailsArea.getText().trim() : "";
                boolean service = serviceBtn.isSelected();

                if (homepageController != null) {
                    homepageController.createRequest(title, details, service);
                } else {
                    Request r = new Request(title, details, service, null);
                    HomepageView.this.addCreatedRequest(r);
                }

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
