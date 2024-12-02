package views;

import domain.entities.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionsView extends JPanel {

    private final JPanel transactionsPanel;

    public TransactionsView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Transaction History");
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));

        add(headerLabel, BorderLayout.NORTH);
        add(new JScrollPane(transactionsPanel), BorderLayout.CENTER);
    }

    public void updateTransactions(List<Transaction> transactions) {
        transactionsPanel.removeAll();

        if (transactions.isEmpty()) {
            JLabel noTransactionsLabel = new JLabel("No transactions found.");
            noTransactionsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
            noTransactionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            transactionsPanel.add(noTransactionsLabel);
        } else {
            // Display most recent transaction at the top
            transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

            for (int i = 0; i < transactions.size(); i++) {
                Transaction transaction = transactions.get(i);
                JPanel transactionCard = new JPanel(new BorderLayout());
                transactionCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));

                // Highlight the most recent transaction
                if (i == 0) {
                    transactionCard.setBackground(new Color(230, 255, 230)); // Light green background
                } else {
                    transactionCard.setBackground(Color.WHITE);
                }

                JLabel typeLabel = new JLabel(transaction.getType().toUpperCase());
                typeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
                typeLabel.setForeground(transaction.getType().equalsIgnoreCase("BUY")
                        ? new Color(76, 175, 80) // Green for BUY
                        : new Color(244, 67, 54)); // Red for SELL

                JLabel cryptoLabel = new JLabel(transaction.getCryptoName());
                cryptoLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
                cryptoLabel.setForeground(new Color(33, 150, 243));

                JLabel amountLabel = new JLabel(String.format("Amount: %.6f", transaction.getAmount()));
                amountLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

                JLabel priceLabel = new JLabel(String.format("Price: $%.2f", transaction.getPrice()));
                priceLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JLabel dateLabel = new JLabel("Date: " + dateFormat.format(transaction.getDate()));
                dateLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
                dateLabel.setForeground(Color.GRAY);

                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.setBackground(transactionCard.getBackground());
                detailsPanel.add(cryptoLabel);
                detailsPanel.add(amountLabel);
                detailsPanel.add(priceLabel);
                detailsPanel.add(dateLabel);

                transactionCard.add(typeLabel, BorderLayout.WEST);
                transactionCard.add(detailsPanel, BorderLayout.CENTER);

                transactionsPanel.add(transactionCard);
                transactionsPanel.add(Box.createVerticalStrut(10));
            }
        }

        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }
}
