package view.start_interface;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;
import use_case.start.signup.SignupState;
import view.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;

import entity.Gender;

public class SignUpView extends JPanel {
	private SignupController signupController;
	private final SignupViewModel signupViewModel;

	private static final int PANEL_WIDTH = 420;
	private static final int PANEL_HEIGHT = 580;
	private static final int INPUT_HEIGHT = 45;
	private static final int BUTTON_HEIGHT = 50;
	private static final int COMPONENT_SPACING = 15;
	private static final int SECTION_SPACING = 25;
	private static final int ERROR_SPACING = 5;

	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;

	private JLabel firstNameLabel;
	private JLabel lastNameLabel;
	private JLabel emailLabel;
	private JLabel passwordLabel;
	private JLabel confirmPasswordLabel;
	private JLabel genderLabel;

	private JLabel firstNameErrorLabel;
	private JLabel lastNameErrorLabel;
	private JLabel emailErrorLabel;
	private JLabel passwordErrorLabel;
	private JLabel confirmPasswordErrorLabel;
	private JLabel generalErrorLabel;
	private JLabel genderErrorLabel;

	private JButton getVerifiedButton;
	private JLabel loginLinkLabel;

	private HashMap<String, Gender> genderOptions;
	private JComboBox<String> genderComboBox;

	private String viewName;

	public SignUpView(SignupViewModel signupViewModel) {
		this.viewName = "sign up";
		this.signupViewModel = signupViewModel;
		initGender();
		initializeComponents();
		setupStyling();
		addEventListeners();
		bindViewModel();
	}

	private void initializeComponents() {
		JLabel titleLabel = new JLabel("Create Your Neighbourly Account", SwingConstants.CENTER);
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
		titleLabel.setForeground(UIConstants.textColor);

		firstNameLabel = createStyledLabel("First Name");
		lastNameLabel = createStyledLabel("Last Name");
		emailLabel = createStyledLabel("Email Address");
		passwordLabel = createStyledLabel("Password");
		confirmPasswordLabel = createStyledLabel("Confirm Password");
		genderLabel = createStyledLabel("Gender");

		firstNameField = createStyledTextField("Enter your first name");
		lastNameField = createStyledTextField("Enter your last name");
		emailField = createStyledTextField("Enter your email");
		passwordField = createStyledPasswordField("Create a password");
		confirmPasswordField = createStyledPasswordField("Re-enter your password");

		firstNameErrorLabel = createErrorLabel("");
		lastNameErrorLabel = createErrorLabel("");
		emailErrorLabel = createErrorLabel("");
		passwordErrorLabel = createErrorLabel("");
		confirmPasswordErrorLabel = createErrorLabel("");
		generalErrorLabel = createErrorLabel("");
		genderErrorLabel = createErrorLabel("");
		generalErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		buildGenderDropdown();

		getVerifiedButton = createStyledButton("Get Verfied");
		loginLinkLabel = createStyledLink("<html>Already a neighbour? <u>Log in.</u></html>");

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(30, 40, 30, 40));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setOpaque(false);

		JPanel titlePanel = createCenteredPanel(titleLabel);
		titlePanel.setBorder(new EmptyBorder(0, 0, SECTION_SPACING, 0));

		JPanel formPanel = buildFormPanel();

		mainPanel.add(titlePanel);
		mainPanel.add(formPanel);

		add(mainPanel, BorderLayout.CENTER);
	}

	private void initGender() {
		this.genderOptions = new HashMap<>();
		for (Gender gender: Gender.values()) {
			genderOptions.put(gender.getLabel(), gender);
		}
	}

	private JPanel buildNamePanel() {
		JPanel row = new JPanel(new GridBagLayout());
		row.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.insets = new Insets(0, 0, 0, COMPONENT_SPACING);

		c.gridx = 0;
		row.add(createNameColumn(firstNameLabel, firstNameErrorLabel, firstNameField), c);

		c.gridx = 1;
		c.insets = new Insets(0, COMPONENT_SPACING, 0, 0);
		row.add(createNameColumn(lastNameLabel, lastNameErrorLabel, lastNameField), c);

		return row;
	}

	private JPanel buildFormPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;

		gbc.gridy = row++;
		gbc.insets = new Insets(0, 0, ERROR_SPACING, 0);
		panel.add(generalErrorLabel, gbc);

		gbc.gridy = row++;
		gbc.insets = new Insets(COMPONENT_SPACING, 0, COMPONENT_SPACING, 0);
		panel.add(buildNamePanel(), gbc);
		row = addField(panel, gbc, row, emailLabel, emailErrorLabel, emailField);
		row = addField(panel, gbc, row, genderLabel, genderErrorLabel, genderComboBox);
		row = addField(panel, gbc, row, passwordLabel, passwordErrorLabel, passwordField);
		row = addField(panel, gbc, row, confirmPasswordLabel, confirmPasswordErrorLabel, confirmPasswordField);

		gbc.gridy = row++;
		gbc.insets = new Insets(SECTION_SPACING, 0, COMPONENT_SPACING, 0);
		panel.add(getVerifiedButton, gbc);

		gbc.gridy = row;
		gbc.insets = new Insets(10, 0, 0, 0);
		panel.add(loginLinkLabel, gbc);

		return panel;
	}

	private void buildGenderDropdown() {
		genderComboBox = new JComboBox<>(genderOptions.keySet().toArray(new String[0]));
		genderComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
		genderComboBox.setBackground(Color.WHITE);
		genderComboBox.setForeground(UIConstants.darkGray);
	}

	private int addField(JPanel panel, GridBagConstraints gbc, int row,
			JLabel label, JLabel errorLabel, JComponent field) {
		gbc.gridy = row++;
		gbc.insets = new Insets(COMPONENT_SPACING, 0, 5, 0);
		panel.add(label, gbc);

		gbc.gridy = row++;
		gbc.insets = new Insets(ERROR_SPACING, 0, 0, 0);
		panel.add(errorLabel, gbc);

		gbc.gridy = row++;
		gbc.insets = new Insets(ERROR_SPACING, 0, 0, 0);
		panel.add(field, gbc);

		return row;
	}

	private void setupStyling() {
		setBackground(UIConstants.backgroundColor);
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
	}

	private void addEventListeners() {
		addPlaceholderBehavior(firstNameField, "Enter your first name");
		addPlaceholderBehavior(lastNameField, "Enter your last name");
		addPlaceholderBehavior(emailField, "Enter your email");
		addPlaceholderBehavior(passwordField, "Create a password");
		addPlaceholderBehavior(confirmPasswordField, "Re-enter your password");

		handleTextUpdates();
		handleSignupButton();
		handleGenderSelection();
		handleKeyboardSubmit();
		handleLoginLinkNavigation();
	}

	private void handleTextUpdates() {
		attachDocumentListener(firstNameField, value -> {
			SignupState state = signupViewModel.getState();
			state.setFirstName(value);
			signupViewModel.setState(state);
		});

		attachDocumentListener(lastNameField, value -> {
			SignupState state = signupViewModel.getState();
			state.setLastName(value);
			signupViewModel.setState(state);
		});

		attachDocumentListener(emailField, value -> {
			SignupState state = signupViewModel.getState();
			state.setEmail(value);
			signupViewModel.setState(state);
		});

		attachDocumentListener(passwordField, value -> {
			SignupState state = signupViewModel.getState();
			state.setPassword(value);
			signupViewModel.setState(state);
		});

		attachDocumentListener(confirmPasswordField, value -> {
			SignupState state = signupViewModel.getState();
			state.setConfirmPassword(value);
			signupViewModel.setState(state);
		});
	}

	private void handleGenderSelection() {
		genderComboBox.addActionListener(e -> {
			String selectedGender = (String) genderComboBox.getSelectedItem();
			SignupState state = signupViewModel.getState();
			state.setGender(genderOptions.get(selectedGender));
			signupViewModel.setState(state);
		});
	}

	private void attachDocumentListener(JTextField field, java.util.function.Consumer<String> consumer) {
		field.getDocument().addDocumentListener(new DocumentListener() {
			private void handleChange() {
				String value = field instanceof JPasswordField
						? new String(((JPasswordField) field).getPassword())
						: field.getText();
				consumer.accept(value.trim());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				handleChange();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				handleChange();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				handleChange();
			}
		});
	}

	private void handleSignupButton() {
		getVerifiedButton.addActionListener(e -> handleSignupRequest());
	}

	private void handleKeyboardSubmit() {
		KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();
		inputMap.put(enterKey, "signup");
		actionMap.put("signup", new AbstractAction() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				handleSignupRequest();
			}
		});
	}

	private void handleLoginLinkNavigation() {
		loginLinkLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				signupController.switchToLogin();
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				loginLinkLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				loginLinkLabel.setForeground(UIConstants.signUpTextColor);
			}
		});
	}

	private void handleSignupRequest() {
		if (signupController == null) {
			SignupState state = signupViewModel.getState();
			state.setGeneralError("Please connect a signup controller.");
			signupViewModel.setState(state);
			updateErrorLabels();
			return;
		}

		SignupState currentState = signupViewModel.getState();
		signupController.execute(
				currentState.getFirstName(),
				currentState.getLastName(),
				currentState.getEmail(),
				currentState.getPassword(),
				currentState.getConfirmPassword(),
				currentState.getGender());
	}

	private void bindViewModel() {
		signupViewModel.addPropertyChangeListener(evt -> SwingUtilities.invokeLater(this::updateErrorLabels));
	}

	private void updateErrorLabels() {
		SignupState state = signupViewModel.getState();
		if (state == null) {
			return;
		}

		firstNameErrorLabel.setText(state.getFirstNameError());
		lastNameErrorLabel.setText(state.getLastNameError());
		emailErrorLabel.setText(state.getEmailError());
		passwordErrorLabel.setText(state.getPasswordError());
		confirmPasswordErrorLabel.setText(state.getConfirmPasswordError());
		generalErrorLabel.setText(state.getGeneralError());
	}

	private JLabel createStyledLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		label.setForeground(UIConstants.textColor);
		return label;
	}

	private JLabel createErrorLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(UIConstants.errorFontStyle);
		label.setForeground(UIConstants.errorLoginColor);
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
		textField.setForeground(UIConstants.textColorFaded);
		textField.setText(placeholder);
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
		passwordField.setForeground(UIConstants.textColorFaded);
		passwordField.setEchoChar((char) 0);
		passwordField.setText(placeholder);
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

	private JLabel createStyledLink(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(UIConstants.signUpTextColor);
		label.setFont(UIConstants.fontStyle);
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return label;
	}

	private JPanel createCenteredPanel(JComponent component) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setOpaque(false);
		panel.add(component);
		return panel;
	}

	private void addPlaceholderBehavior(JTextField field, String placeholder) {
		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (field.getText().equals(placeholder)) {
					field.setText("");
					field.setForeground(Color.BLACK);
					if (field instanceof JPasswordField) {
						((JPasswordField) field).setEchoChar('•');
					}
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().isEmpty()) {
					field.setText(placeholder);
					field.setForeground(UIConstants.textColorFaded);
					if (field instanceof JPasswordField) {
						((JPasswordField) field).setEchoChar((char) 0);
					}
				}
			}
		});
	}

	public void setSignupController(SignupController signupController) {
		this.signupController = signupController;
	}

	public String getViewName() {
		return this.viewName;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
	}

	private JPanel createNameColumn(JLabel label, JLabel error, JComponent field) {
		JPanel column = new JPanel();
		column.setOpaque(false);
		column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
		column.add(label);
		column.add(Box.createVerticalStrut(ERROR_SPACING));
		column.add(error);
		column.add(Box.createVerticalStrut(ERROR_SPACING));
		column.add(field);
		return column;
	}
}
