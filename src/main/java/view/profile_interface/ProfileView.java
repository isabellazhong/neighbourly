package view.profile_interface;

import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileViewModel;
import use_case.profile.ProfileState;
import view.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileView extends JPanel {
    public static final String FONT_NAME = "SansSerif";
    private final String viewName = "profile";

    private ProfileController profileController;
    private ProfileViewModel profileViewModel;

    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 600;
    private static final int INPUT_HEIGHT = 45;
    private static final int BUTTON_HEIGHT = 50;
    private static final int COMPONENT_SPACING = 15;
    private static final int SECTION_SPACING = 25;

    private JTextField nameInputField;
    private JTextField lastNameInputField;
    private JTextField emailInputField;
    private JComboBox<String> genderComboBox;
    private JButton saveButton;
    private JButton backButton;
    private JLabel pageTitleLabel;
    private JLabel nameLabel;
    private JLabel lastNameLabel;
    private JLabel emailLabel;
    private JLabel genderLabel;
    private JLabel statusLabel;

    public ProfileView(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
        initializeComponents();
        setupLayout();
        setupStyling();
        addEventListeners();
        profileViewModel.addPropertyChangeListener(evt -> {
            String propertyName = evt.getPropertyName();
            if (propertyName == null || propertyName.equals("state")) {
                SwingUtilities.invokeLater(() -> updateFields());
            } else if (propertyName.equals("error")) {
                SwingUtilities.invokeLater(() -> showError());
            }
        });
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
        if (profileController != null) {
            profileController.loadProfile();
        }
    }

    private void initializeComponents() {
        pageTitleLabel = new JLabel("Profile", SwingConstants.CENTER);
        pageTitleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 30));
        pageTitleLabel.setForeground(UIConstants.darkGray);

        nameLabel = createStyledLabel("First Name");
        nameInputField = createStyledTextField("");

        lastNameLabel = createStyledLabel("Last Name");
        lastNameInputField = createStyledTextField("");

        emailLabel = createStyledLabel("Email");
        emailInputField = createStyledTextField("");
        emailInputField.setEditable(false);
        emailInputField.setBackground(new Color(240, 240, 240));

        genderLabel = createStyledLabel("Gender");
        genderComboBox = new JComboBox<>(new String[]{"female", "male"});
        genderComboBox.setPreferredSize(new Dimension(300, INPUT_HEIGHT));
        genderComboBox.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        genderComboBox.setBackground(Color.WHITE);
        genderComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.textColorFaded, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        saveButton = createStyledButton("Save Changes");
        backButton = createStyledButton("Back");

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        JPanel titlePanel = createCenteredPanel(pageTitleLabel);
        titlePanel.setBorder(new EmptyBorder(0, 0, SECTION_SPACING * 2, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(nameInputField, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(lastNameLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(lastNameInputField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(emailLabel, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(emailInputField, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(genderLabel, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, SECTION_SPACING, 0);
        formPanel.add(genderComboBox, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(saveButton, gbc);

        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(backButton, gbc);

        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(statusLabel, gbc);

        mainPanel.add(titlePanel);
        mainPanel.add(formPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupStyling() {
        setBackground(UIConstants.textFieldBackground);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    private void addEventListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBack();
            }
        });
    }

    private void handleSave() {
        String name = nameInputField.getText().trim();
        String lastName = lastNameInputField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();

        if (name.isEmpty()) {
            showStatus("Name cannot be empty", UIConstants.errorColor);
            nameInputField.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            showStatus("Last name cannot be empty", UIConstants.errorColor);
            lastNameInputField.requestFocus();
            return;
        }

        if (profileController != null) {
            profileController.execute(name, lastName, gender);
        } else {
            showStatus("Error: Controller not connected", UIConstants.errorColor);
        }
    }

    private void handleBack() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            topFrame.dispose();
        }
    }

    private void updateFields() {
        ProfileState state = profileViewModel.getState();
        if (state != null) {
            String name = state.getName();
            String lastName = state.getLastName();
            String email = state.getEmail();
            String gender = state.getGender();
            
            if (name != null) {
                nameInputField.setText(name);
            }
            if (lastName != null) {
                lastNameInputField.setText(lastName);
            }
            if (email != null) {
                emailInputField.setText(email);
            }
            if (gender != null) {
                genderComboBox.setSelectedItem(gender);
            }
            
            if (state.getProfileError() == null && name != null && !name.isEmpty()) {
                String message = "Profile updated successfully!";
                if (state.getWarning() != null) {
                    message += " " + state.getWarning();
                }
                showStatus(message, UIConstants.successColor);
            }
        }
    }

    private void showError() {
        ProfileState state = profileViewModel.getState();
        if (state.getProfileError() != null) {
            showStatus(state.getProfileError(), UIConstants.errorColor);
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        label.setForeground(UIConstants.darkGray);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, INPUT_HEIGHT));
        textField.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.textColorFaded, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, BUTTON_HEIGHT));
        button.setFont(new Font(FONT_NAME, Font.BOLD, 16));

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);

        button.setBackground(UIConstants.primaryGreen);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(UIConstants.primaryGreenHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(UIConstants.primaryGreen);
            }
        });

        return button;
    }

    private JPanel createCenteredPanel(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.add(component);
        return panel;
    }

    public String getViewName() {
        return viewName;
    }
}

