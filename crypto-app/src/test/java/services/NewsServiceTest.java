package services;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class NewsServiceTest {

    private NewsService newsService = new NewsService();

    @Test
    public void testFetchNewsRealAPI() throws Exception {
        // Assuming the NewsService is configured to hit a real or test API
        List<String> news = newsService.fetchNews("latest");

        // Log the output for debugging
        System.out.println("API returned news: " + news);

        // Basic checks to ensure the structure and presence of the data
        assertNotNull(news, "The news list should not be null.");
        assertFalse(news.isEmpty(), "The news list should not be empty.");

        // Example of a more detailed structural check (if applicable)
        // This checks if the first item in the list is a String and not empty,
        // which is more general than checking for specific content.
        assertAll("Verify content structure and presence",
                () -> assertTrue(news.get(0) instanceof String, "The first item should be a String."),
                () -> assertFalse(news.get(0).isEmpty(), "The first item should not be empty.")
        );
    }
}
