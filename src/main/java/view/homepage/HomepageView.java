package view.homepage;

import view.navigator.PageNavigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomepageView extends JPanel {
    private final PageNavigator navigator;

    public HomepageView(PageNavigator navigator) {
        this.navigator = navigator;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        setPreferredSize(new Dimension(1100, 700));
    }


    private JComponent buildCenter() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField search = new JTextField();
        search.setFont(search.getFont().deriveFont(24f));
        search.setPreferredSize(new Dimension(900, 64));

        JButton searchBtn = new JButton("Search Requests");

        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.add(search, BorderLayout.CENTER);
        row.add(searchBtn, BorderLayout.EAST);

        gbc.gridy = 0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        center.add(new JPanel(), gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(row, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        center.add(new JPanel(), gbc);

        return center;
    }

    private JComponent buildBottomBar() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);

        JButton createBtn = new JButton("+");
        createBtn.setFont(createBtn.getFont().deriveFont(Font.PLAIN, 28f));
        createBtn.setPreferredSize(new Dimension(64, 64));
        createBtn.setToolTipText("Create");

        createBtn.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem requestItem = new JMenuItem("Create New Request");
            JMenuItem offerItem = new JMenuItem("Offer Help");

            Window owner = SwingUtilities.getWindowAncestor(this);
            requestItem.addActionListener(evt -> navigator.openCreateRequest(owner));
            offerItem.addActionListener(evt -> navigator.openCreateOffer(owner));

            menu.add(requestItem);
            menu.add(offerItem);

            int yOffset = -menu.getPreferredSize().height;
            menu.show(createBtn, 0, yOffset);
        });


        bottom.add(createBtn);
        return bottom;
    }
}
