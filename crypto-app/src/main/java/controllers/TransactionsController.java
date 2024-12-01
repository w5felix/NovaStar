package controllers;

import entities.Transaction;
import entities.User;
import interactors.UserService;
import views.TransactionsView;

import javax.swing.*;
import java.util.List;

public class TransactionsController {

    private final UserService userService;
    private final User currentUser;
    private final TransactionsView transactionsView;

    public TransactionsController(UserService userService, User currentUser, TransactionsView transactionsView) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.transactionsView = transactionsView;
    }

    public JPanel getView() {
        loadTransactions();
        return transactionsView;
    }

    private void loadTransactions() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<Transaction> transactions = currentUser.getTransactions();
                transactionsView.updateTransactions(transactions);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(transactionsView, "Error loading transactions: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void refresh() throws Exception {
        transactionsView.updateTransactions(userService.getTransactions(currentUser));

    }
}
