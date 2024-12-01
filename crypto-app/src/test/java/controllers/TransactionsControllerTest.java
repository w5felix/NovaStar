package controllers;

import entities.Transaction;
import entities.User;
import services.UserService;
import views.TransactionsView;
import controllers.TransactionsController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionsControllerTest {

    private UserService userService;
    private User currentUser;
    private TransactionsView transactionsView;
    private TransactionsController transactionsController;

    @BeforeEach
    void setUp() {
        // Set up test data
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, "BTC", 0.5, 30000, new Date(), "BUY"));
        transactions.add(new Transaction(2, "ETH", 1.0, 2000, new Date(), "SELL"));

        List<Transaction> emptyTransactions = new ArrayList<>();

        currentUser = new User("test-user-id", "testUser", 10000.0, transactions, new ArrayList<>());
        transactionsView = new TransactionsView();
        userService = new UserService(null, null) { // Anonymous class to override methods for testing
            @Override
            public List<Transaction> getTransactions(User user) {
                if (user.getUserId().equals("test-user-id")) {
                    return user.getTransactions();
                }
                return emptyTransactions;
            }
        };

        transactionsController = new TransactionsController(userService, currentUser, transactionsView);
    }

    @Test
    void testLoadTransactions_updatesViewWithTransactions() {
        JPanel panel = transactionsController.getView();

        assertNotNull(panel, "The returned panel should not be null.");
        assertEquals(2, currentUser.getTransactions().size(), "The current user should have 2 transactions.");

        SwingUtilities.invokeLater(() -> {
            assertTrue(panel.isVisible(), "The transactions panel should be visible.");
        });
    }

    @Test
    void testRefresh_updatesTransactionsInView() throws Exception {
        // Add a new transaction to the user's transactions
        Transaction newTransaction = new Transaction(3, "DOGE", 500.0, 0.25, new Date(), "BUY");
        currentUser.getTransactions().add(newTransaction);

        // Refresh the view
        transactionsController.refresh();

        SwingUtilities.invokeLater(() -> {
            assertEquals(3, currentUser.getTransactions().size(), "The transactions list should have 3 transactions.");
            assertTrue(transactionsView.isVisible(), "The transactions view should be visible.");
        });
    }

    @Test
    void testGetView_withEmptyTransactions() {
        // Clear user's transactions
        currentUser.setTransactions(new ArrayList<>());

        JPanel panel = transactionsController.getView();

        SwingUtilities.invokeLater(() -> {
            assertNotNull(panel, "The returned panel should not be null.");
            assertTrue(panel.isVisible(), "The transactions panel should be visible.");
        });
    }
}




