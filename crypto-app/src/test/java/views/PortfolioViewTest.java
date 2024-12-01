package views;


import entities.PortfolioEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioViewTest {

    private PortfolioView portfolioView;

    @Mock
    private PortfolioView.PortfolioDataHandler mockDataHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        portfolioView = new PortfolioView();
    }

    @Test
    void testUpdatePortfolioWithEmptyList() {
        portfolioView.updatePortfolio(Collections.emptyList(), mockDataHandler);

        // Check that the panel contains a message about no holdings
        Component[] components = portfolioView.getComponents();
        assertNotNull(components);
        assertTrue(components.length > 0);

        JScrollPane scrollPane = (JScrollPane) components[0];
        JPanel portfolioPanel = (JPanel) scrollPane.getViewport().getView();
        assertEquals(1, portfolioPanel.getComponentCount());

        JLabel messageLabel = (JLabel) portfolioPanel.getComponent(0);
        assertEquals("No cryptocurrency holdings found.", messageLabel.getText());
    }

    @Test
    void testUpdatePortfolioWithData() {
        List<PortfolioEntry> portfolio = Arrays.asList(
                new PortfolioEntry("BTC", 2),
                new PortfolioEntry("ETH", 5)
        );

        when(mockDataHandler.getCurrentValue(any(PortfolioEntry.class))).thenAnswer(invocation -> {
            PortfolioEntry entry = invocation.getArgument(0);
            return entry.getCryptoName().equals("BTC") ? 40000.0 : 2000.0; // Explicit Double
        });

        when(mockDataHandler.getInitialInvestment(any(PortfolioEntry.class))).thenAnswer(invocation -> {
            PortfolioEntry entry = invocation.getArgument(0);
            return entry.getCryptoName().equals("BTC") ? 30000.0 : 1500.0; // Explicit Double
        });

        portfolioView.updatePortfolio(portfolio, mockDataHandler);

        // Check that the panel contains the correct number of portfolio cards
        Component[] components = portfolioView.getComponents();
        assertNotNull(components);
        assertTrue(components.length > 0);

        JScrollPane scrollPane = (JScrollPane) components[0];
        JPanel portfolioPanel = (JPanel) scrollPane.getViewport().getView();
        assertEquals(4, portfolioPanel.getComponentCount()); // 2 cards + 2 vertical struts

        // Check the content of the first portfolio card
        JPanel btcCard = (JPanel) portfolioPanel.getComponent(0);
        JLabel btcNameLabel = (JLabel) btcCard.getComponent(0); // Crypto name
        JLabel btcValueLabel = (JLabel) btcCard.getComponent(1); // Value and growth
        assertEquals("BTC", btcNameLabel.getText());
        assertTrue(btcValueLabel.getText().contains("Value: $40000.00"));
        assertTrue(btcValueLabel.getText().contains("Growth: 33.33%"));
    }

    @Test
    void testShowError() {
        String errorMessage = "An error occurred!";
        portfolioView.showError(errorMessage);

        // Use JOptionPane to confirm the error message (interactive test)
        // This test may require mocking JOptionPane if automation is required
    }
}

