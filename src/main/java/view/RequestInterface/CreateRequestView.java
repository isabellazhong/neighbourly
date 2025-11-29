package view.RequestInterface;
import entity.Request;

import javax.swing.*;
import java.awt.*;

public class CreateRequestView extends JDialog {
    public CreateRequestView(Window owner) {
        super(owner, "Create Request", ModalityType.APPLICATION_MODAL);
        initUI();
        pack();
        setLocationRelativeTo(owner);
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
        column.add(Box.createVerticalStrut(6));

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        column.add(errorLabel);
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
        JButton backBtn = new JButton("Back");
        JButton postBtn = new JButton("Post Request");

        backBtn.addActionListener(e -> dispose());
        postBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String details = detailsArea.getText().trim();
            boolean isService = serviceBtn.isSelected();

            if (title.isEmpty()) {
                errorLabel.setText("Title is required.");
                titleField.requestFocusInWindow();
                pack();
                return;
            }

            errorLabel.setText(" ");
            Request req = new Request(title, details, isService, null);
            System.out.println("Created Request: " + req.getId() + " Title: " + req.getTitle());
            dispose();
        });

        buttons.add(backBtn);
        buttons.add(postBtn);


        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);
    }
}
