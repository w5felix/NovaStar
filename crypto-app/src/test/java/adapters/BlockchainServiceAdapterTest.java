package adapters;

import api.BlockChainAPIClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BlockchainServiceAdapterTest {

    private BlockchainServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BlockchainServiceAdapter();
        // Initialize the mockito static mocking
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentPrice_ReturnsCorrectPrice() throws Exception {
        try (MockedStatic<BlockChainAPIClient> mockedApi = mockStatic(BlockChainAPIClient.class)) {
            mockedApi.when(() -> BlockChainAPIClient.getCurrentPrice("BTC"))
                    .thenReturn(50000.0f);

            float expected = 50000.0f;
            float actual = adapter.getCurrentPrice("BTC");
            assertEquals(expected, actual, "The returned price should match the API call result.");
        }
    }

    @Test
    void getCurrentPrice_ThrowsException() {
        try (MockedStatic<BlockChainAPIClient> mockedApi = mockStatic(BlockChainAPIClient.class)) {
            mockedApi.when(() -> BlockChainAPIClient.getCurrentPrice("BTC"))
                    .thenThrow(new RuntimeException("Failed to fetch price"));

            assertThrows(RuntimeException.class, () -> adapter.getCurrentPrice("BTC"),
                    "Should throw the same exception as the API client");
        }
    }
}
