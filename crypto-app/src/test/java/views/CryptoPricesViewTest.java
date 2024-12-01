package views;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import api.BlockChainAPIClient.CryptoInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoPricesViewTest {

    private CryptoPricesView view;

    @BeforeEach
    public void setUp() {
        // Initialize the CryptoPricesView before each test
        view = new CryptoPricesView();
    }

    @Test
    public void testViewInitialization() {
        // Verify that the CryptoPricesView initializes with the correct layout and components
        assertNotNull(view, "View should not be null");
        assertTrue(view.getLayout() instanceof BorderLayout, "View should use BorderLayout");
        assertEquals(2, view.getComponentCount(), "View should have two components (header and scroll pane)");
    }

    @Test
    public void testUpdateCryptoPricesWithEmptyList() {
        // Pass an empty list to updateCryptoPrices and verify that the panel displays a message
        List<CryptoInfo> emptyList = new ArrayList<>();
        view.updateCryptoPrices(emptyList, crypto -> fail("No action should be triggered on an empty list"));

        JPanel cryptoPricesPanel = (JPanel) ((JScrollPane) view.getComponent(1)).getViewport().getView();
        assertEquals(1, cryptoPricesPanel.getComponentCount(), "Crypto prices panel should display one message");
        JLabel messageLabel = (JLabel) cryptoPricesPanel.getComponent(0);
        assertEquals("No cryptocurrency prices available.", messageLabel.getText(), "Empty list message is incorrect");
    }

    @Test
    public void testUpdateCryptoPricesWithData() {
        // Mock data to simulate a list of cryptocurrency prices
        List<CryptoInfo> mockData = new ArrayList<>();
        mockData.add(new CryptoInfo("Bitcoin", "BTC", (float) 60000.00F, 5.0F));
        mockData.add(new CryptoInfo("Ethereum", "ETH", 4000.00F, -2.0F));

        view.updateCryptoPrices(mockData, crypto -> {
            // Handle click action (for testing purposes, verify the correct item is clicked)
            assertEquals("Bitcoin", crypto.getName(), "First crypto in the list should be Bitcoin");
        });

        JPanel cryptoPricesPanel = (JPanel) ((JScrollPane) view.getComponent(1)).getViewport().getView();
        assertEquals(2, cryptoPricesPanel.getComponentCount() / 2, "Crypto prices panel should display two items (with spacers)");

        // Verify the contents of the first item
        JPanel firstCryptoCard = (JPanel) cryptoPricesPanel.getComponent(0);
        JLabel nameLabel = (JLabel) firstCryptoCard.getComponent(0); // First component in the card
        assertEquals("Bitcoin (BTC)", nameLabel.getText(), "First card name label is incorrect");
    }

    @Test
    public void testCryptoCardClickAction() {
        // Mock data with a single item
        List<CryptoInfo> mockData = new ArrayList<>();
        CryptoInfo bitcoin = new CryptoInfo("Bitcoin", "BTC", 60000.00F, 5.0F);
        mockData.add(bitcoin);

        final boolean[] wasClicked = {false};
        view.updateCryptoPrices(mockData, crypto -> {
            wasClicked[0] = true;
            assertEquals("Bitcoin", crypto.getName(), "Clicked crypto name should match Bitcoin");
        });

        JPanel cryptoPricesPanel = (JPanel) ((JScrollPane) view.getComponent(1)).getViewport().getView();
        JPanel firstCryptoCard = (JPanel) cryptoPricesPanel.getComponent(0);

        // Simulate a mouse click on the first crypto card
        for (java.awt.event.MouseListener ml : firstCryptoCard.getMouseListeners()) {
            ml.mouseClicked(null); // Simulate click event
        }

        assertTrue(wasClicked[0], "CryptoActionHandler should be triggered on card click");
    }
}

