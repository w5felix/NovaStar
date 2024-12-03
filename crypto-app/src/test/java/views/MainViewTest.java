package views;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;

public class MainViewTest {

    private MainView mainView;

    @BeforeEach
    public void setUp() {
        mainView = new MainView();
    }

    @Test
    public void testInitialization() {
        assertNotNull(mainView, "MainView should be initialized");
        assertNotNull(mainView.getTabbedPane(), "TabbedPane should be initialized");
        assertEquals(0, mainView.getTabbedPane().getTabCount(), "TabbedPane should initially have no tabs");
    }

    @Test
    public void testUpdateCashReserves() {
        mainView.updateCashReserves(1234.56);
        JLabel cashReservesLabel = (JLabel) ((JPanel) mainView.getComponent(0)).getComponent(2); // Access header
        assertEquals("Cash Reserves: $1234.56", cashReservesLabel.getText(),
                "Cash reserves label should display the correct value");
    }

    @Test
    public void testUpdatePortfolioValue() {
        mainView.updatePortfolioValue(5678.90);
        JLabel portfolioValueLabel = (JLabel) ((JPanel) mainView.getComponent(0)).getComponent(3); // Access header
        assertEquals("Total Portfolio Value: $5678.90", portfolioValueLabel.getText(),
                "Portfolio value label should display the correct value");
    }

    @Test
    public void testSetDepositAction() {
        final boolean[] actionTriggered = {false};
        mainView.setDepositAction(e -> actionTriggered[0] = true);
        JButton depositButton = (JButton) ((JPanel) mainView.getComponent(2)).getComponent(0); // Access footer
        depositButton.doClick();
        assertTrue(actionTriggered[0], "Deposit button action should trigger correctly");
    }

    @Test
    public void testSetWithdrawAction() {
        final boolean[] actionTriggered = {false};
        mainView.setWithdrawAction(e -> actionTriggered[0] = true);
        JButton withdrawButton = (JButton) ((JPanel) mainView.getComponent(2)).getComponent(1); // Access footer
        withdrawButton.doClick();
        assertTrue(actionTriggered[0], "Withdraw button action should trigger correctly");
    }

    @Test
    public void testSetBuyCryptoAction() {
        final boolean[] actionTriggered = {false};
        mainView.setBuyCryptoAction(e -> actionTriggered[0] = true);
        JButton buyCryptoButton = (JButton) ((JPanel) mainView.getComponent(2)).getComponent(2); // Access footer
        buyCryptoButton.doClick();
        assertTrue(actionTriggered[0], "Buy Crypto button action should trigger correctly");
    }

    @Test
    public void testAddTabs() {
        JPanel portfolioPanel = new JPanel();
        JPanel transactionsPanel = new JPanel();
        JPanel cryptoPricesPanel = new JPanel();
        JPanel newsPanel = new JPanel();

        mainView.addPortfolioPanel(portfolioPanel);
        mainView.addTransactionsPanel(transactionsPanel);
        mainView.addCryptoPricesPanel(cryptoPricesPanel);
        mainView.addNewsPanel(newsPanel);

        JTabbedPane tabbedPane = mainView.getTabbedPane();

        assertEquals(4, tabbedPane.getTabCount(), "TabbedPane should have 4 tabs");
        assertEquals("Portfolio", tabbedPane.getTitleAt(0), "First tab should be Portfolio");
        assertEquals("Transactions", tabbedPane.getTitleAt(1), "Second tab should be Transactions");
        assertEquals("Crypto Prices", tabbedPane.getTitleAt(2), "Third tab should be Crypto Prices");
        assertEquals("News Search", tabbedPane.getTitleAt(3), "Fourth tab should be News Search");
    }
}

