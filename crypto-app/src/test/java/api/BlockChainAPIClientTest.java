package api;

import api.BlockChainAPIClient;
import entities.PortfolioEntry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BlockChainAPIClientTest {

    @Test
    public void testFetchPopularCryptos() {
        try {
            List<BlockChainAPIClient.CryptoInfo> cryptos = BlockChainAPIClient.fetchPopularCryptos();
            assertNotNull(cryptos, "The crypto list should not be null.");
            assertFalse(cryptos.isEmpty(), "The crypto list should not be empty.");
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testGetCurrentPrice() {
        try {
            float btcPrice = BlockChainAPIClient.getCurrentPrice("BTC-USD");
            assertTrue(btcPrice > 0, "BTC price should be greater than zero.");
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testPortfolioValueCalculation() {
        try {
            // Mock prices for BTC and ETH
            float btcPrice = BlockChainAPIClient.getCurrentPrice("BTC-USD");
            float ethPrice = BlockChainAPIClient.getCurrentPrice("ETH-USD");

            // Create portfolio entries
            PortfolioEntry btcEntry = new PortfolioEntry("BTC", 1.0);
            PortfolioEntry ethEntry = new PortfolioEntry("ETH", 2.0);

            // Calculate portfolio value
            double totalValue = btcEntry.getAmount() * btcPrice + ethEntry.getAmount() * ethPrice;

            // Assert value is calculated correctly
            assertEquals(totalValue, btcPrice + 2 * ethPrice, 0.01, "Portfolio value should match expected calculation.");

        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

}

