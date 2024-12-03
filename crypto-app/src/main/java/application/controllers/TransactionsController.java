package application.controllers;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domain.entities.Transaction;
import domain.entities.User;
import application.interactors.get_transactions.GetTransactionsInteractor;
import views.TransactionsView;

public class TransactionsController {

    private final GetTransactionsInteractor getTransactionsInteractor;
    private final TransactionsView transactionsView;
    private final User currentUser;

    /**
     * Constructor for TransactionsController.
     *
     * @param getTransactionsInteractor Interactor to fetch transactions.
     * @param currentUser               The currently logged-in user.
     * @param transactionsView          The view for displaying transactions.
     */
    public TransactionsController(GetTransactionsInteractor getTransactionsInteractor,
                                  User currentUser,
                                  TransactionsView transactionsView) {
        this.getTransactionsInteractor = getTransactionsInteractor;
        this.currentUser = currentUser;
        this.transactionsView = transactionsView;
    }

    /**
     * Returns the transactions view.
     *
     * @return JPanel representing the transactions view.
     */
    public JPanel getView() {
        loadTransactions();
        return transactionsView;
    }

    /**
     * Loads transactions and updates the view.
     */
    private void loadTransactions() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Fetch transactions using the interactor
                List<Transaction> transactions = getTransactionsInteractor.execute(currentUser);
                transactionsView.updateTransactions(transactions);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(transactionsView, "Error loading transactions: " + exception.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Refreshes the transactions view by fetching the latest transactions.
     */
    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Fetch updated transactions using the interactor
                List<Transaction> transactions = getTransactionsInteractor.execute(currentUser);
                transactionsView.updateTransactions(transactions);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(transactionsView, "Error refreshing transactions: " + exception.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
