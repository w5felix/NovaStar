package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.INewsService;
import views.NewsSearchView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NewsSearchControllerTest {

    private NewsSearchController newsSearchController;
    private TestNewsService testNewsService;
    private TestNewsSearchView testNewsSearchView;

    @BeforeEach
    void setUp() {
        testNewsService = new TestNewsService();
        testNewsSearchView = new TestNewsSearchView();
        newsSearchController = new NewsSearchController(testNewsService, testNewsSearchView);
    }

    @Test
    void testHandleSearchWithValidQuery() throws InterruptedException {
        // Simulate user input
        testNewsSearchView.setSearchQuery("crypto");

        // Trigger search and wait for completion
        triggerSearchAndWait();

        // Assert that the displayed news matches the expected results
        List<String> expected = List.of(
                "Crypto News 1: http://example.com/crypto1",
                "Crypto News 2: http://example.com/crypto2"
        );
        assertEquals(expected, testNewsSearchView.getDisplayedNews());
    }

    @Test
    void testHandleSearchWithEmptyQuery() throws InterruptedException {
        // Simulate empty user input
        testNewsSearchView.setSearchQuery("");

        // Trigger search and wait for completion
        triggerSearchAndWait();

        // Assert that the displayed news indicates the user needs to enter a query
        assertEquals(List.of("Please enter a search query."), testNewsSearchView.getDisplayedNews());
    }

    @Test
    void testHandleSearchWithServiceError() throws InterruptedException {
        // Simulate user input
        testNewsSearchView.setSearchQuery("error");

        // Trigger search and wait for completion
        triggerSearchAndWait();

        // Assert that the displayed news indicates an error
        assertEquals(List.of("Error fetching news: Simulated service error"), testNewsSearchView.getDisplayedNews());
    }

    private void triggerSearchAndWait() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            newsSearchController.handleSearch();
            latch.countDown();
        });
        latch.await();
        // Small wait to allow the Swing thread to update the display
        Thread.sleep(100);
    }

    // Test implementation of INewsService
    static class TestNewsService implements INewsService {

        @Override
        public List<String> fetchNews(String query) throws Exception {
            if ("error".equals(query)) {
                throw new Exception("Simulated service error");
            } else if ("crypto".equals(query)) {
                return List.of(
                        "Crypto News 1: http://example.com/crypto1",
                        "Crypto News 2: http://example.com/crypto2"
                );
            } else {
                return List.of();
            }
        }
    }

    // Test implementation of NewsSearchView
    static class TestNewsSearchView extends NewsSearchView {
        private String searchQuery;
        private final List<String> displayedNews = new ArrayList<>();

        @Override
        public String getSearchQuery() {
            return searchQuery;
        }

        @Override
        public void displayNews(List<String> news) {
            synchronized (displayedNews) {
                displayedNews.clear();
                displayedNews.addAll(news);
            }
        }

        public void setSearchQuery(String searchQuery) {
            this.searchQuery = searchQuery;
        }

        public List<String> getDisplayedNews() {
            synchronized (displayedNews) {
                return new ArrayList<>(displayedNews);
            }
        }
    }
}
