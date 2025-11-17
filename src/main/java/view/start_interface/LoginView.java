package main.java.view.start_interface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import main.java.interface_adapter.login.LoginController;
import main.java.interface_adapter.login.LoginViewModel;
import main.java.use_case.login.LoginState;
import main.java.view.UIConstants;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginView extends JPanel {
    private LoginController loginController; 

    private final int PANEL_WIDTH = 400;
    private final int PANEL_HEIGHT = 500;
    private final int INPUT_HEIGHT = 45;
    private final int BUTTON_HEIGHT = 50;
    private final int COMPONENT_SPACING = 15;
    private final int SECTION_SPACING = 25;

    private JTextField emailInputField;
    private JPasswordField passwordInputField;
    private JButton loginButton;
    private JLabel titleLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel statusLabel;

    private LoginViewModel loginViewModel;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel; 

        initializeComponents();
        setupLayout();
        setupStyling();
        addEventListeners();
    }

    private void initializeComponents() {
        titleLabel = new JLabel("Neighbourly", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(UIConstants.textColor);

        emailLabel = createStyledLabel("Email Address");
        emailInputField = createStyledTextField("Enter your email");

        passwordLabel = createStyledLabel("Password");
        passwordInputField = createStyledPasswordField("Enter your password");

        loginButton = createStyledButton("Log In");

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        JPanel titlePanel = createCenteredPanel(titleLabel);
        titlePanel.setBorder(new EmptyBorder(0, 0, SECTION_SPACING * 2, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(emailLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(emailInputField, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, SECTION_SPACING, 0);
        formPanel.add(passwordInputField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(loginButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(statusLabel, gbc);

        mainPanel.add(titlePanel);
        mainPanel.add(formPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupStyling() {
        setBackground(UIConstants.backgroundColor);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    private void addEventListeners() {
        addPlaceholderBehavior(emailInputField, "Enter your email");

        addPlaceholderBehavior(passwordInputField, "Enter your password");

        loginButton.addActionListener(e -> handleLogin());

        KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(enterKey, "login");
        actionMap.put("login", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleLogin();
            }
        });
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(UIConstants.textColor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, INPUT_HEIGHT));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.textColorFaded, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);

        textField.setText(placeholder);
        textField.setForeground(UIConstants.textColorFaded);

        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, INPUT_HEIGHT));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.textColorFaded, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordField.setEchoChar((char) 0);

        passwordField.setText(placeholder);
        passwordField.setForeground(UIConstants.textColorFaded);

        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, BUTTON_HEIGHT));
        button.setFont(new Font("SansSerif", Font.BOLD, 16));

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);

        button.setBackground(new Color(76, 175, 80));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(69, 160, 73));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(76, 175, 80));
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

    private void addPlaceholderBehavior(JTextField textField, String placeholder) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    if (textField instanceof JPasswordField) {
                        ((JPasswordField) textField).setEchoChar('•');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(UIConstants.textColorFaded);
                    textField.setText(placeholder);
                    if (textField instanceof JPasswordField) {
                        ((JPasswordField) textField).setEchoChar((char) 0);
                    }
                }
            }
        });
    }

    private void handleLogin() {
        String email = getEmailText();
        String password = getPasswordText();
        final LoginState currentState = new LoginState();

        if (email.isEmpty() || email.equals("Enter your email")) {
            showStatus("Please enter your email address", Color.RED);
            emailInputField.requestFocus();
            return;
        }

        if (password.isEmpty() || password.equals("Enter your password")) {
            showStatus("Please enter your password", Color.RED);
            passwordInputField.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            showStatus("Please enter a valid email address", Color.RED);
            emailInputField.requestFocus();
            return;
        }

        currentState.setPassword(password);
        loginViewModel.setState(currentState);
        loginController.execute(
            currentState.getemail(),
            currentState.getPassword()
        ); 

        
    }

    private String getEmailText() {
        String text = emailInputField.getText();
        return text.equals("Enter your email") ? "" : text;
    }

    private String getPasswordText() {
        String text = new String(passwordInputField.getPassword());
        return text.equals("Enter your password") ? "" : text;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController; 
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    }

}
