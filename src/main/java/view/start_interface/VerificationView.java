package view.start_interface;

import interface_adapter.verification.VerificationController;
import interface_adapter.verification.VerificationViewModel;
import use_case.start.id_verification.VerificationViewState;
import view.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

public class VerificationView extends JPanel {
    private final String viewName;
    private final VerificationViewModel verificationViewModel;
    private VerificationController verificationController;

    private JLabel titleLabel;
    private JPanel dropZonePanel;
    private JLabel dropZonePromptLabel;
    private JLabel selectedFileLabel;
    private JButton browseButton;
    private JButton verifyButton;
    private JButton continueButton;
    private JLabel statusLabel;
    private JLabel errorLabel;

    public VerificationView(VerificationViewModel verificationViewModel) {
        this.viewName = "verification view";
        this.verificationViewModel = verificationViewModel;
        initializeComponents();
        layoutComponents();
        registerEventHandlers();
        bindViewModel();
    }

    public String getViewName() {
        return viewName;
    }

    public void setVerificationController(VerificationController verificationController) {
        this.verificationController = verificationController;
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.backgroundColor);
        setBorder(new EmptyBorder(40, 60, 40, 60));

        titleLabel = new JLabel("Verify your identity", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(UIConstants.textColor);

        dropZonePanel = new JPanel(new BorderLayout());
        dropZonePanel.setBorder(new LineBorder(UIConstants.textColorFaded, 2, true));
        dropZonePanel.setPreferredSize(new Dimension(400, 200));

        dropZonePromptLabel = new JLabel("Drag & drop your ID image here", SwingConstants.CENTER);
        dropZonePromptLabel.setForeground(UIConstants.darkGray);
        dropZonePromptLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        selectedFileLabel = new JLabel("No file selected", SwingConstants.CENTER);
        selectedFileLabel.setForeground(UIConstants.darkGray);

        browseButton = createPrimaryButton("Choose file");
        verifyButton = createPrimaryButton("Verify ID");
        verifyButton.setEnabled(false);

        continueButton = createPrimaryButton("Continue");
        continueButton.setVisible(false);
        continueButton.setEnabled(false);

        statusLabel = new JLabel("Upload a government-issued ID to continue.", SwingConstants.CENTER);
        statusLabel.setForeground(UIConstants.textColor);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(UIConstants.errorColor);

        enableFileDrop(dropZonePanel);
    }

    private void layoutComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        dropZonePanel.add(dropZonePromptLabel, BorderLayout.CENTER);
        dropZonePanel.add(selectedFileLabel, BorderLayout.SOUTH);

        JPanel dropZoneWrapper = new JPanel(new BorderLayout());
        dropZoneWrapper.setOpaque(false);
        dropZoneWrapper.add(dropZonePanel, BorderLayout.CENTER);

        contentPanel.add(dropZoneWrapper);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonRow = new JPanel();
        buttonRow.setOpaque(false);
        buttonRow.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonRow.add(browseButton);
        buttonRow.add(verifyButton);
        buttonRow.add(continueButton);

        contentPanel.add(buttonRow);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(errorLabel);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void registerEventHandlers() {
        browseButton.addActionListener(e -> {
            File file = showFileChooser();
            if (file != null) {
                handleFileSelection(file);
            }
        });

        verifyButton.addActionListener(e -> handleVerifyRequest());
        continueButton.addActionListener(e -> {
            if (verificationController != null) {
                VerificationViewState currentState = verificationViewModel.getState();
                verificationController.execute(currentState.getSelectedFilePath(), currentState.getSignupInputData());
            } else {
                setErrorMessage("Verification flow not connected yet.");
            }
        });
    }

    private void bindViewModel() {
        verificationViewModel.addPropertyChangeListener(evt -> SwingUtilities.invokeLater(this::refreshFromState));
        refreshFromState();
    }

    private void refreshFromState() {
        VerificationViewState state = verificationViewModel.getState();
        if (state == null) {
            state = new VerificationViewState();
            verificationViewModel.setState(state);
        }

        selectedFileLabel.setText(state.getSelectedFileName());
        selectedFileLabel.setForeground(UIConstants.darkGray);

        statusLabel.setText(state.getStatusMessage());
        errorLabel.setText(state.getErrorMessage() == null ? "" : state.getErrorMessage());

        if (state.isVerifying()) {
            statusLabel.setForeground(UIConstants.textColorFaded);
        } else if (state.isVerificationSuccessful()) {
            statusLabel.setForeground(UIConstants.successColor);
        } else {
            statusLabel.setForeground(UIConstants.textColor);
        }

        verifyButton.setEnabled(state.getSelectedFilePath() != null && !state.isVerifying());
        continueButton.setVisible(state.isVerificationSuccessful());
        continueButton.setEnabled(state.isVerificationSuccessful());
    }

    private void handleFileSelection(File file) {
        if (file == null) {
            return;
        }

        VerificationViewState currentState = ensureState();
        currentState.setSelectedFileName(file.getName());
        currentState.setSelectedFilePath(file.getAbsolutePath());
        currentState.setStatusMessage("File ready. Click verify to continue.");
        currentState.setErrorMessage("");
        currentState.resetVerificationFlags();

        verificationViewModel.setState(currentState);
        verificationViewModel.firePropertyChange();
    }

    private void handleVerifyRequest() {
        VerificationViewState state = ensureState();
        if (state.getSelectedFilePath() == null) {
            setErrorMessage("Please select a file before verifying.");
            return;
        }

        if (verificationController == null) {
            setErrorMessage("Verification flow not connected yet.");
            return;
        }

        state.setVerifying(true);
        state.setStatusMessage("Verifying your document...");
        state.setErrorMessage("");
        verificationViewModel.setState(state);
        verificationViewModel.firePropertyChange();
        VerificationViewState verificationViewState = verificationViewModel.getState();

        verificationController.execute(state.getSelectedFilePath(), verificationViewState.getSignupInputData());
    }

    private void setErrorMessage(String message) {
        VerificationViewState state = ensureState();
        state.setErrorMessage(message);
        state.setVerifying(false);
        verificationViewModel.setState(state);
        verificationViewModel.firePropertyChange();
    }

    private VerificationViewState ensureState() {
        VerificationViewState state = verificationViewModel.getState();
        if (state == null) {
            state = new VerificationViewState();
            verificationViewModel.setState(state);
        }
        return state;
    }

    private void enableFileDrop(JComponent component) {
        new DropTarget(component, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = dtde.getTransferable();
                try {
                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        @SuppressWarnings("unchecked")
                        List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        if (!files.isEmpty()) {
                            handleFileSelection(files.get(0));
                        }
                    }
                } catch (Exception ex) {
                    setErrorMessage("Unable to read dropped file.");
                } finally {
                    dtde.dropComplete(true);
                }
            }
        }, true);
    }

    private File showFileChooser() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(UIConstants.loginButtonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(UIConstants.fontStyle);
        button.setPreferredSize(new Dimension(140, 45));

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

    public void setController(VerificationController controller) {
        verificationController = controller; 
    }
}