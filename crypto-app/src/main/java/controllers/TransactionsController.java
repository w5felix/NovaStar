package controllers;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import entities.Transaction;
import entities.User;
import interactors.UserService;
import views.TransactionsView;

public class TransactionsController {

    private final UserService userService;
    private final User currentUser;
    private final TransactionsView transactionsView;

    public TransactionsController(UserService userService, User currentUser, TransactionsView transactionsView) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.transactionsView = transactionsView;
    }

    /**
     * Transaction view.
     * @return JPanel.
     */
    public JPanel getView() {
        loadTransactions();
        return transactionsView;
    }

    private void loadTransactions() {
        SwingUtilities.invokeLater(() -> {
            try {
                final List<Transaction> transactions = currentUser.getTransactions();
                transactionsView.updateTransactions(transactions);
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(transactionsView, "Error loading transactions: " + exception.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Refresh function.
     * @throws Exception exception.
     */
    public void refresh() throws Exception {
        transactionsView.updateTransactions(userService.getTransactions(currentUser));

    }
}
