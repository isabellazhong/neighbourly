package messaging;

import messaging.interface_adapter.MessagingController;
import messaging.interface_adapter.MessagingViewModel;
import messaging.use_case.MessagingState;
import messaging.MessageDTO;
import view.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessagingView extends JPanel implements PropertyChangeListener {
    private final MessagingViewModel messagingViewModel;
    private MessagingController messagingController;
    
    private JTextArea messagesArea;
    private JScrollPane messagesScrollPane;
    private JTextField messageInputField;
    private JButton sendButton;
    private JLabel errorLabel;
    
    private String currentChannelId;
    private String currentUserId;
    private long lastMessageTimestamp = 0;
    private ScheduledExecutorService refreshExecutor;
    
    public MessagingView(MessagingViewModel messagingViewModel) {
        this.messagingViewModel = messagingViewModel;
        this.messagingViewModel.addPropertyChangeListener(this);
        
        initializeComponents();
        setupLayout();
        setupStyling();
        addEventListeners();
        
        // Start auto-refresh timer
        startAutoRefresh();
    }
    
    private void initializeComponents() {
        messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        messagesArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messagesArea.setBackground(Color.WHITE);
        messagesArea.setForeground(Color.BLACK);
        
        messagesScrollPane = new JScrollPane(messagesArea);
        messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messagesScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.textColorFaded, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        messageInputField = new JTextField();
        messageInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.textColorFaded, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        messageInputField.setBackground(Color.WHITE);
        messageInputField.setForeground(Color.BLACK);
        
        sendButton = createStyledButton("Send");
        
        errorLabel = new JLabel("");
        errorLabel.setFont(UIConstants.errorFontStyle);
        errorLabel.setForeground(UIConstants.errorColor);
        errorLabel.setVisible(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Messages area (center, takes most space)
        add(messagesScrollPane, BorderLayout.CENTER);
        
        // Input area (bottom)
        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(messageInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        // Error label
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.setOpaque(false);
        errorPanel.add(errorLabel);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setOpaque(false);
        bottomPanel.add(errorPanel, BorderLayout.NORTH);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupStyling() {
        setBackground(UIConstants.backgroundColor);
        setPreferredSize(new Dimension(600, 500));
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.fontStyle);
        button.setPreferredSize(new Dimension(100, 40));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(UIConstants.loginButtonColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
    
    private void addEventListeners() {
        // Send button action
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSendMessage();
            }
        });
        
        // Enter key in text field
        messageInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSendMessage();
            }
        });
    }
    
    private void handleSendMessage() {
        String messageText = messageInputField.getText().trim();
        
        if (messageText.isEmpty()) {
            return;
        }
        
        if (currentChannelId == null) {
            // Show error message to user
            errorLabel.setText("No channel available. Please accept a request or offer first to start messaging.");
            errorLabel.setVisible(true);
            return;
        }
        
        if (messagingController == null) {
            errorLabel.setText("Messaging controller not initialized.");
            errorLabel.setVisible(true);
            return;
        }
        
        if (currentUserId == null) {
            errorLabel.setText("User ID not set. Cannot send message.");
            errorLabel.setVisible(true);
            return;
        }
        
        messagingController.sendMessage(currentChannelId, currentUserId, messageText);
        messageInputField.setText("");
    }
    
    private void startAutoRefresh() {
        refreshExecutor = Executors.newSingleThreadScheduledExecutor();
        refreshExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (currentChannelId != null && messagingController != null) {
                    SwingUtilities.invokeLater(() -> {
                        // Use lastMessageTimestamp if > 0, otherwise use 0 to fetch all messages
                        long timestamp = lastMessageTimestamp > 0 ? lastMessageTimestamp : 0;
                        messagingController.refreshMessages(currentChannelId, timestamp);
                    });
                }
            }
        }, 2, 2, TimeUnit.SECONDS);
    }
    
    private void stopAutoRefresh() {
        if (refreshExecutor != null && !refreshExecutor.isShutdown()) {
            refreshExecutor.shutdown();
        }
    }
    
    private void updateMessagesDisplay(List<MessageDTO> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        for (MessageDTO message : messages) {
            sb.append("[").append(message.getUserId()).append("]: ")
              .append(message.getMessage())
              .append("\n");
            
            // Update last timestamp
            if (message.getTimestamp() > lastMessageTimestamp) {
                lastMessageTimestamp = message.getTimestamp();
            }
        }
        
        // Append to existing messages or replace if it's a full refresh
        String currentText = messagesArea.getText();
        if (currentText.isEmpty()) {
            messagesArea.setText(sb.toString());
        } else {
            messagesArea.append(sb.toString());
        }
        
        // Auto-scroll to bottom
        messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            MessagingState state = messagingViewModel.getState();
            
            // Update channel ID
            if (state.getChannelId() != null && !state.getChannelId().isEmpty()) {
                // If channel changed, reset timestamp
                if (currentChannelId == null || !state.getChannelId().equals(currentChannelId)) {
                    lastMessageTimestamp = 0;
                    messagesArea.setText("");
                }
                currentChannelId = state.getChannelId();
            }
            
            // Update messages
            if (state.getMessages() != null && !state.getMessages().isEmpty()) {
                // If this is the first load (lastMessageTimestamp is 0), replace all messages
                // Otherwise, we're refreshing and should append new ones
                if (lastMessageTimestamp == 0) {
                    messagesArea.setText("");
                }
                updateMessagesDisplay(state.getMessages());
            }
            
            // Update error
            if (state.getError() != null && !state.getError().isEmpty()) {
                errorLabel.setText(state.getError());
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);
            }
        }
    }
    
    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }
    
    public void setMessagingController(MessagingController messagingController) {
        this.messagingController = messagingController;
    }
    
    public void cleanup() {
        stopAutoRefresh();
    }
}
