package views;

import interface_adapterss.ViewModel;

import javax.swing.*;
import java.awt.*;
import java.lang.classfile.CodeModel;

public class AfterLoginView extends JPanel {

    private final ViewModel viewmodel;

    private JButton transaction = new JButton("transaction");

    private JButton balance = new JButton("account balance");

    private JButton exchangeRate = new JButton("exchange rate");

    private JButton newsSearch = new JButton("news search");

    private JButton logout = new JButton("log out");

    public AfterLoginView(ViewModel viewmodel) {
        this.viewmodel = viewmodel;

        setLayout(new BorderLayout());

        JLabel line1 = new JLabel("Username:" /*+ viewmodel.getCurrentUsername()*/);
        line1.setHorizontalAlignment(SwingConstants.CENTER);
        line1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(line1, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        panel.add(transaction);
        panel.add(balance);
        panel.add(exchangeRate);
        panel.add(newsSearch);

        add(panel, BorderLayout.CENTER);

        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logoutPanel.add(logout);

        add(logoutPanel, BorderLayout.SOUTH);

        logout.addActionListener(e -> {
            this.viewmodel.setCurrentState(ViewModel.LOGIN_VIEW);
        });
    }
}
