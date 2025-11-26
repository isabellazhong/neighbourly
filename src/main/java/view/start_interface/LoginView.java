package view.start_interface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.DocumentEvent;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginViewModel;
import use_case.start.login.LoginState;
import view.UIConstants;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JPanel {
    private LoginController loginController;
    private final String viewName = "log in"; 

    private final int PANEL_WIDTH = 400;
    private final int PANEL_HEIGHT = 500;
    private final int INPUT_HEIGHT = 45;
    private final int BUTTON_HEIGHT = 50;
    private final int COMPONENT_SPACING = 15;
    private final int ERROR_SPACING = 5;
    private final int SECTION_SPACING = 25;

    private JTextField emailInputField;
    private JPasswordField passwordInputField;
    private JButton loginButton;
    private JLabel titleLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel loginErrorLabel;
    private JLabel passwordErrorLabel;

    // clean architecture dependencies
    private LoginViewModel loginViewModel;

    // sign up button
    private JLabel signUpText;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        initializeComponents();
        setupLayout();
        setupStyling();
        addEventListeners();
        bindViewModel();
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

        signUpText = createStyledSignUp("<html>Don't have account with us? <u> Sign up.</u></html>");
        loginErrorLabel = createErrorLabel("");
        passwordErrorLabel = createErrorLabel("");
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
        gbc.insets = new Insets(ERROR_SPACING, 0, 0, 0);
        formPanel.add(loginErrorLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(emailInputField, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(ERROR_SPACING, 0, 0, 0);
        formPanel.add(passwordErrorLabel, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, SECTION_SPACING, 0);
        formPanel.add(passwordInputField, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, COMPONENT_SPACING, 0);
        formPanel.add(loginButton, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(30, 0, COMPONENT_SPACING, 0);
        formPanel.add(signUpText, gbc);

        mainPanel.add(titlePanel);
        mainPanel.add(formPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupStyling() {
        setBackground(UIConstants.backgroundColor);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    private void handleEmailInputUpdates() {
        emailInputField.getDocument().addDocumentListener(new DocumentListener() {
            public void documentEventListener() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setEmail(emailInputField.getText());
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentEventListener();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentEventListener();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentEventListener();
            }
        });
    }

    private void handlePasswordInputUpdates() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            public void documentEventListener() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentEventListener();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentEventListener();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentEventListener();
            }
        });
    }

    private void handleLogin() {
        loginButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(loginButton)) {
                            final LoginState currentState = loginViewModel.getState();
                            loginViewModel.setState(currentState);
                            updateErrorLabels();

                            loginController.execute(
                                    currentState.getEmail(),
                                    currentState.getPassword());
                            
                        }
                    }
                });
    }

    public void handleSignUp() {
        signUpText.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signUpText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginController.switchToSignUp();
            }
        });
    }

    private void addEventListeners() {
        handleSignUp();
        handleEmailInputUpdates();
        handlePasswordInputUpdates();
        handleLogin();

        KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(enterKey, "login");
        actionMap.put("login", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleEmailInputUpdates();
                handlePasswordInputUpdates();
                handleLogin();
            }
        });

        addPlaceholderBehavior(emailInputField, "Enter your email");
        addPlaceholderBehavior(passwordInputField, "Enter your password");

    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(UIConstants.textColor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createErrorLabel(String error) {
        JLabel label = new JLabel(error);
        label.setFont(UIConstants.errorFontStyle);
        label.setForeground(UIConstants.errorLoginColor);
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
        button.setFont(UIConstants.fontStyle);

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);

        button.setBackground(UIConstants.loginButtonColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(UIConstants.loginButtonColorHovered);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(UIConstants.loginButtonColor);
            }
        });

        return button;
    }

    private JLabel createStyledSignUp(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UIConstants.signUpTextColor);
        label.setFont(UIConstants.fontStyle);
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                label.setForeground(UIConstants.signUpTextColor);
            }
        });
        return label;
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
                        JPasswordField field = (JPasswordField) textField;
                        field.setEchoChar('•');
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(UIConstants.textColorFaded);
                    textField.setText(placeholder);
                    if (textField instanceof JPasswordField) {
                        JPasswordField field = (JPasswordField) textField;
                        field.setEchoChar((char) 0);
                    }
                }
            }
        });
    }

    private void updateErrorLabels() {
        LoginState currentState = loginViewModel.getState();
        loginErrorLabel.setText(currentState.getLoginError());
        passwordErrorLabel.setText(currentState.getPasswordError());
    }

	private void bindViewModel() {
		loginViewModel.addPropertyChangeListener(evt -> SwingUtilities.invokeLater(this::updateErrorLabels));
	}

    public String getViewName() {
        return this.viewName; 
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    }
}
