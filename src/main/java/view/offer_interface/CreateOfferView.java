package view.offer_interface;

import entity.Offer;
import interface_adapter.offers.create_offer.CreateOfferController;
import interface_adapter.offers.edit_offer.EditOfferController;
import view.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.UUID;

public class CreateOfferView extends JPanel {
    public static final String FONT_NAME = "SansSerif";
    private final String viewName = "create offer";

    private CreateOfferController createOfferController;
    private EditOfferController editOfferController;

    private UUID editingOfferId = null;

    private static final int PANEL_WIDTH = 720;
    private static final int PANEL_HEIGHT = 460;
    private static final int INPUT_HEIGHT = 100;
    private static final int INPUT_WIDTH = 500;
    private static final int BUTTON_HEIGHT = 60;
    private static final int COMPONENT_SPACING = 15;
    private static final int SECTION_SPACING = 25;
    private static final String DETAILS_PLACEHOLDER = "Add more details (optional)";
    private static final String TITLE_PLACEHOLDER = "What are you offering?";
    private static final Color SUCCESS = UIConstants.darkGray;

    private JTextField titleInputField;
    private JTextArea detailsInputField;
    private JButton submitButton;
    private JLabel pageTitleLabel;
    private JLabel titleLabel;
    private JLabel detailsLabel;
    private JLabel statusLabel;

    public CreateOfferView() {
        initializeComponents();
        setupLayout();
        setupStyling();
        addEventListeners();
    }

    public void setCreateOfferController(CreateOfferController createOfferController) {
        this.createOfferController = createOfferController;
    }

    public void setEditOfferController(EditOfferController editOfferController) {
        this.editOfferController = editOfferController;
    }

    public void loadOfferToEdit(Offer offer) {
        this.editingOfferId = offer.getId();
        pageTitleLabel.setText("Edit Offer");
        submitButton.setText("Save Changes");
        titleInputField.setText(offer.getTitle());
        titleInputField.setForeground(Color.BLACK);
        detailsInputField.setText(offer.getAlternativeDetails());
        detailsInputField.setForeground(Color.BLACK); }


    private void initializeComponents() {
        pageTitleLabel = new JLabel("Offer Help", SwingConstants.CENTER);
        pageTitleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 30));
        pageTitleLabel.setForeground(UIConstants.darkGray);

        titleLabel = createStyledLabel("Offer Title");
        titleInputField = createStyledTextField(TITLE_PLACEHOLDER);

        detailsLabel = createStyledLabel("Description");
        detailsInputField = createStyledTextArea(DETAILS_PLACEHOLDER);

        submitButton = createStyledButton("Submit Offer");

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 12));

        statusLabel.setPreferredSize(new Dimension(INPUT_WIDTH, 20));
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

        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(titleInputField, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(detailsLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, SECTION_SPACING, 0);
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JScrollPane detailsScrollPane = new JScrollPane(detailsInputField);
        detailsScrollPane.setPreferredSize(new Dimension(INPUT_WIDTH, 150));
        detailsScrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.textColorFaded, 1));
        formPanel.add(detailsScrollPane, gbc);

        gbc.weighty = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(submitButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
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
        addPlaceholderBehavior(titleInputField, TITLE_PLACEHOLDER);
        addPlaceholderBehavior(detailsInputField, DETAILS_PLACEHOLDER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
    }

    private void handleSubmit() {
        String title = getTitleText();
        String details = getDetailsText();

        if (title.isEmpty()) {
            showStatus("Please enter a title for your offer", UIConstants.errorLoginColor);
            titleInputField.requestFocus();
            return;
        }

        if (editingOfferId != null) {
            if (editOfferController != null) {
                editOfferController.execute(editingOfferId, title, details);
                showStatus("Offer updated successfully", UIConstants.darkGray);
            }
        } else {
            if (createOfferController != null) {
                createOfferController.execute(title, details);
                showStatus("Offer created successfully", UIConstants.darkGray);
                resetFields();
            }
        }

        if (createOfferController != null) {
            createOfferController.execute(title, details);
            showStatus("Offer submitted successfully", UIConstants.darkGray);
            resetFields();
        } else {
            System.out.println("Error: Controller not connected to View");
        }
    }

    private void resetFields() {
        titleInputField.setText(TITLE_PLACEHOLDER);
        titleInputField.setForeground(UIConstants.textColorFaded);
        detailsInputField.setText(DETAILS_PLACEHOLDER);
        detailsInputField.setForeground(UIConstants.textColorFaded);
        editingOfferId = null;
    }

    private void clearStatus() {
        statusLabel.setText(" ");
        statusLabel.revalidate();
        statusLabel.repaint();
    }

    private String getTitleText() {
        String text = titleInputField.getText();
        return text.equals(TITLE_PLACEHOLDER) ? "" : text;
    }

    private String getDetailsText() {
        String text = detailsInputField.getText();
        return text.equals(DETAILS_PLACEHOLDER) ? "" : text;
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        statusLabel.revalidate();
        statusLabel.repaint();
        this.revalidate();
        this.repaint();
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
        textField.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));
        textField.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.textColorFaded, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);

        textField.setText(placeholder);
        textField.setForeground(UIConstants.textColorFaded);

        return textField;
    }

    private JTextArea createStyledTextArea(String placeholder) {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setText(placeholder);
        textArea.setForeground(UIConstants.textColorFaded);

        return textArea;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(INPUT_WIDTH, BUTTON_HEIGHT));
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

    private void addPlaceholderBehavior(JTextComponent textComponent, String placeholder) {
        textComponent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (statusLabel.getForeground().equals(SUCCESS)) {
                    clearStatus();
                }
                if (textComponent.getText().equals(placeholder)) {
                    textComponent.setText("");
                    textComponent.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textComponent.getText().isEmpty()) {
                    textComponent.setForeground(UIConstants.textColorFaded);
                    textComponent.setText(placeholder);
                }
            }
        });
    }

    public String getViewName() {
        return viewName;
    }
}