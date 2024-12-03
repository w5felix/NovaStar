package api;

import api.MarketAuxAPIClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MarketAuxAPIClientTest {

    @Test
    public void testFetchNews() {
        String query = "Bitcoin";
        List<String> newsArticles = MarketAuxAPIClient.fetchNews(query);

        assertNotNull(newsArticles, "News articles list should not be null.");
        assertFalse(newsArticles.isEmpty(), "News articles list should not be empty.");
        for (String article : newsArticles) {
            assertTrue(article.contains("http"), "Each article should contain a valid URL.");
        }
    }

    @Test
    public void testInvalidQuery() {
        String invalidQuery = ""; // Empty query
        List<String> newsArticles = MarketAuxAPIClient.fetchNews(invalidQuery);

        // Assert that the result is not null
        assertNotNull(newsArticles, "News articles list should not be null.");

        // Adjust the test based on the expected behavior
        if (invalidQuery.trim().isEmpty()) {
            // Validate the returned list if default results are expected
            assertFalse(newsArticles.isEmpty(), "News articles list should contain default results for an empty query.");
        } else {
            // Validate the returned list is empty for invalid input (if required)
            assertTrue(newsArticles.isEmpty(), "News articles list should be empty for an invalid query.");
        }
    }

    @Test
    public void testSearchNewsIntegration() {
        try {
            // Fetch news articles
            List<String> articles = MarketAuxAPIClient.fetchNews("Ethereum");

            // Ensure articles are fetched and contain relevant keywords
            assertFalse(articles.isEmpty(), "News articles list should not be empty.");
            for (String article : articles) {
                assertTrue(article.toLowerCase().contains("ethereum"), "Article should be related to Ethereum.");
            }

        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }
}

