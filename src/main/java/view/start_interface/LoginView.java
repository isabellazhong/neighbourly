package main.java.view.start_interface;

import javax.swing.*;

import main.java.view.UIConstants;

import java.awt.*;


public class LoginView extends JPanel {
	private final String viewName = "log in";
    
    // dimensions  
    private final int loginWidth = 50; 
    private final int loginHeight = loginWidth*2;
    private final int inputLength = loginWidth*3/4;
    private final int inputHeight = loginHeight/8;  

    private JTextField emailInputField; 
    private JTextField passwordInputField; 

    private JLabel emailLabel;
    private JLabel passwordLabel;

    private void setTextFieldUI(Panel panel, JLabel label, JTextField textField) {
        panel.add(label);
        panel.add(textField);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.textFieldBackground);
        panel.setFont(new Font("Serif", Font.PLAIN, inputHeight));

    }

    private JLabel setMainTextUI(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UIConstants.textColor);
        return label;
    }

    public LoginView() {

        emailInputField = new JTextField("example@gmail.com", inputLength);
        emailInputField.setSelectedTextColor(UIConstants.textColorFaded);
        passwordInputField = new JTextField(inputLength); 

        emailLabel = setMainTextUI("Email:");
        passwordLabel = setMainTextUI("Password:");


        final Panel emailInfo = new Panel();
        setTextFieldUI(emailInfo, emailLabel, emailInputField);

        final Panel passwordInfo = new Panel();
        setTextFieldUI(passwordInfo, passwordLabel, passwordInputField);

        final Panel infoPanel = new Panel();
        infoPanel.add(emailInfo);
        infoPanel.add(passwordInfo); 
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        this.add(infoPanel);
        this.setBackground(UIConstants.backgroundColor);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(loginWidth, loginHeight); 
    }

}
