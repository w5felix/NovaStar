package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView extends JPanel {

    private final JLabel cashReservesLabel = new JLabel("Cash Reserves: $0.00");
    private final JLabel totalPortfolioValueLabel = new JLabel("Total Portfolio Value: $0.00");
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final JButton depositButton = new JButton("Deposit Cash");
    private final JButton withdrawButton = new JButton("Withdraw Cash");
    private final JButton buyCryptoButton = new JButton("Buy Crypto");
    private final JButton sellCryptoButton = new JButton("Sell Crypto");

    public MainView() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(100, 200, 255)); // Goofy blue background
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("✨ Crypto Portfolio Manager ✨");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(cashReservesLabel);
        headerPanel.add(totalPortfolioValueLabel);

        // Footer with action buttons
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.add(depositButton);
        footerPanel.add(withdrawButton);
        footerPanel.add(buyCryptoButton);
        footerPanel.add(sellCryptoButton);

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void setDepositAction(ActionListener actionListener) {
        depositButton.addActionListener(actionListener);
    }

    public void setWithdrawAction(ActionListener actionListener) {
        withdrawButton.addActionListener(actionListener);
    }

    public void setBuyCryptoAction(ActionListener actionListener) {
        buyCryptoButton.addActionListener(actionListener);
    }

    public void setSellCryptoAction(ActionListener actionListener) { // New setter
        sellCryptoButton.addActionListener(actionListener);
    }

    public void updateCashReserves(double cashReserves) {
        cashReservesLabel.setText(String.format("Cash Reserves: $%.2f", cashReserves));
    }

    public void updatePortfolioValue(double portfolioValue) {
        totalPortfolioValueLabel.setText(String.format("Total Portfolio Value: $%.2f", portfolioValue));
    }

    // Wrapper methods for adding panels
    public void addPortfolioPanel(Component portfolioPanel) {
        tabbedPane.addTab("Portfolio", portfolioPanel);
    }

    public void addTransactionsPanel(Component transactionsPanel) {
        tabbedPane.addTab("Transactions", transactionsPanel);
    }

    public void addCryptoPricesPanel(Component cryptoPricesPanel) {
        tabbedPane.addTab("Crypto Prices", cryptoPricesPanel);
    }

    public void addNewsPanel(Component newsPanel) {
        tabbedPane.addTab("News Search", newsPanel);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
