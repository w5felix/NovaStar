package views;


import entities.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TranscationsViewTest {

    private TransactionsView transactionsView;

    @BeforeEach
    void setUp() {
        transactionsView = new TransactionsView();
    }

    @Test
    void testInitialization() {
        Component[] components = transactionsView.getComponents();
        assertEquals(2, components.length);

        JLabel headerLabel = (JLabel) components[0];
        assertEquals("Transaction History", headerLabel.getText());
        assertEquals(SwingConstants.CENTER, headerLabel.getHorizontalAlignment());

        JScrollPane scrollPane = (JScrollPane) components[1];
        assertNotNull(scrollPane);
        assertTrue(scrollPane.getViewport().getView() instanceof JPanel);
    }

    @Test
    void testUpdateTransactionsWithEmptyList() {
        transactionsView.updateTransactions(Collections.emptyList());

        JPanel transactionsPanel = (JPanel) ((JScrollPane) transactionsView.getComponent(1)).getViewport().getView();
        assertEquals(1, transactionsPanel.getComponentCount());

        JLabel noTransactionsLabel = (JLabel) transactionsPanel.getComponent(0);
        assertEquals("No transactions found.", noTransactionsLabel.getText());
        assertEquals(SwingConstants.CENTER, noTransactionsLabel.getHorizontalAlignment());
    }

    @Test
    void testUpdateTransactionsWithData() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("BTC", 0.5, 30000.0, new Date(System.currentTimeMillis() - 10000), "BUY"),
                new Transaction("ETH", 1.2, 2000.0, new Date(), "SELL")
        );

        transactionsView.updateTransactions(transactions);

        JPanel transactionsPanel = (JPanel) ((JScrollPane) transactionsView.getComponent(1)).getViewport().getView();
        assertEquals(4, transactionsPanel.getComponentCount()); // 2 transaction cards + 2 vertical struts

        // Verify first transaction card (most recent: ETH)
        JPanel firstCard = (JPanel) transactionsPanel.getComponent(0);
        JLabel typeLabelFirst = (JLabel) firstCard.getComponent(0);
        assertEquals("SELL", typeLabelFirst.getText(), "First transaction type should be SELL");

        JPanel detailsPanelFirst = (JPanel) firstCard.getComponent(1);
        JLabel cryptoLabelFirst = (JLabel) detailsPanelFirst.getComponent(0);
        assertEquals("ETH", cryptoLabelFirst.getText(), "First transaction crypto name should be ETH");

        JLabel amountLabelFirst = (JLabel) detailsPanelFirst.getComponent(1);
        assertEquals("Amount: 1.200000", amountLabelFirst.getText(), "First transaction amount should match");

        JLabel priceLabelFirst = (JLabel) detailsPanelFirst.getComponent(2);
        assertEquals("Price: $2000.00", priceLabelFirst.getText(), "First transaction price should match");

        // Verify second transaction card (older: BTC)
        JPanel secondCard = (JPanel) transactionsPanel.getComponent(2);
        JLabel typeLabelSecond = (JLabel) secondCard.getComponent(0);
        assertEquals("BUY", typeLabelSecond.getText(), "Second transaction type should be BUY");

        JPanel detailsPanelSecond = (JPanel) secondCard.getComponent(1);
        JLabel cryptoLabelSecond = (JLabel) detailsPanelSecond.getComponent(0);
        assertEquals("BTC", cryptoLabelSecond.getText(), "Second transaction crypto name should be BTC");

        JLabel amountLabelSecond = (JLabel) detailsPanelSecond.getComponent(1);
        assertEquals("Amount: 0.500000", amountLabelSecond.getText(), "Second transaction amount should match");

        JLabel priceLabelSecond = (JLabel) detailsPanelSecond.getComponent(2);
        assertEquals("Price: $30000.00", priceLabelSecond.getText(), "Second transaction price should match");
    }
}

