package experimental;

import entities.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUIDemoTest {

    @Test
    public void testApplicationLaunch() {
        SwingUtilities.invokeLater(() -> {
            GUIDemo demo = new GUIDemo();
            demo.setVisible(true);

            assertEquals("NovaStar Cryptocurrency Trading Platform\n", demo.getTitle());
            assertTrue(demo.isVisible());
        });
    }

    @Test
    public void testUpdatePortfolioValue() {
        SwingUtilities.invokeLater(() -> {
            GUIDemo demo = new GUIDemo();
            demo.setVisible(true);
            demo.showMainScreen();

            demo.currentUser = new User("123", "testuser", 100.0f, List.of(), List.of());
            JLabel cashReservesLabel = demo.cashReservesLabel;
            JLabel totalPortfolioValueLabel = demo.totalPortfolioValueLabel;

            demo.updatePortfolioValue();

            assertTrue(cashReservesLabel.getText().contains("$100.0"));
            assertTrue(totalPortfolioValueLabel.getText().contains("$100.0"));
        });
    }

    @Test
    public void testDepositCash() {
        SwingUtilities.invokeLater(() -> {
            GUIDemo demo = new GUIDemo();
            demo.setVisible(true);
            demo.showMainScreen();

            demo.currentUser = new User("123", "testuser", 100.0, null, null);
            double initialBalance = demo.currentUser.getCashBalance();

            demo.handleDeposit();

            String inputAmount = "50";
            System.setIn(new java.io.ByteArrayInputStream(inputAmount.getBytes()));

            assertEquals(initialBalance + 50, demo.currentUser.getCashBalance());
        });
    }

    @Test
    public void testLoginScreenComponents() {
        SwingUtilities.invokeLater(() -> {
            GUIDemo demo = new GUIDemo();
            demo.setVisible(true);

            // Switch to login screen and check if expected components exist
            demo.showLoginScreen();

            Container contentPane = demo.getContentPane();
            assertNotNull(contentPane);

            // Check email field
            boolean emailFieldExists = false;
            for (Component component : contentPane.getComponents()) {
                if (component instanceof JTextField) {
                    emailFieldExists = true;
                    break;
                }
            }
            assertTrue(emailFieldExists);

            // Check login button
            boolean loginButtonExists = false;
            for (Component component : contentPane.getComponents()) {
                if (component instanceof JButton) {
                    JButton button = (JButton) component;
                    if ("Login".equals(button.getText())) {
                        loginButtonExists = true;
                        break;
                    }
                }
            }
            assertTrue(loginButtonExists);

            demo.dispose(); // Clean up after test
        });
    }

    @Test
    public void testNewsSearch() {
        SwingUtilities.invokeLater(() -> {
            GUIDemo demo = new GUIDemo();
            demo.setVisible(true);

            // Show main screen and simulate user typing a search query
            demo.showMainScreen();
            demo.newsSearchField.setText("Bitcoin");

            // Simulate pressing the search button
            JButton searchButton = (JButton) demo.newsSearchPanel.getComponent(0); // Assuming first component in search panel is the button
            searchButton.doClick();

            // Verify that results area is updated
            assertNotNull(demo.newsResultsArea);
            String results = demo.newsResultsArea.getText();
            assertFalse(results.isEmpty());
            assertTrue(results.contains("Bitcoin")); // Assuming results contain "Bitcoin"

            demo.dispose(); // Clean up after test
        });
    }
}








