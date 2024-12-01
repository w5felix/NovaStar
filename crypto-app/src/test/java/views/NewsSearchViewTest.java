package views;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class NewsSearchViewTest {

    private NewsSearchView newsSearchView;

    @BeforeEach
    public void setUp() {
        newsSearchView = new NewsSearchView();
    }

    @Test
    public void testInitialization() {
        assertNotNull(newsSearchView, "NewsSearchView should be initialized");
        assertEquals(2, newsSearchView.getComponentCount(), "NewsSearchView should have two main components");
    }

    @Test
    public void testGetSearchQuery() {
        JTextField searchField = (JTextField) ((JPanel) newsSearchView.getComponent(0)).getComponent(1); // Access search field
        searchField.setText("Bitcoin");
        assertEquals("Bitcoin", newsSearchView.getSearchQuery(), "Search query should return the correct input");
    }

    @Test
    public void testDisplayNewsWithArticles() {
        JTextArea resultsArea = (JTextArea) ((JScrollPane) newsSearchView.getComponent(1)).getViewport().getView(); // Access results area
        newsSearchView.displayNews(Arrays.asList("Bitcoin reaches $60k!", "Ethereum 2.0 is live."));
        assertEquals("Bitcoin reaches $60k!\n\nEthereum 2.0 is live.\n\n", resultsArea.getText(),
                "Results area should display all news articles with proper formatting");
    }

    @Test
    public void testDisplayNewsWithNoArticles() {
        JTextArea resultsArea = (JTextArea) ((JScrollPane) newsSearchView.getComponent(1)).getViewport().getView(); // Access results area
        newsSearchView.displayNews(Collections.emptyList());
        assertEquals("No news articles found.", resultsArea.getText(),
                "Results area should display a message when no articles are found");
    }

    @Test
    public void testSetSearchListener() {
        final boolean[] actionTriggered = {false};
        ActionListener mockListener = e -> actionTriggered[0] = true;

        newsSearchView.setSearchListener(mockListener);

        JTextField searchField = (JTextField) ((JPanel) newsSearchView.getComponent(0)).getComponent(1); // Access search field
        searchField.postActionEvent(); // Simulate enter key press

        assertTrue(actionTriggered[0], "Search listener should be triggered when action is performed");
    }

    @Test
    public void testFireSearchEvent() {
        final boolean[] actionTriggered = {false};
        ActionListener mockListener = e -> actionTriggered[0] = true;

        // Add the mock listener
        newsSearchView.setSearchListener(mockListener);

        // Call the fireSearchEvent directly
        newsSearchView.fireSearchEvent();

        // Verify that the action was triggered
        assertTrue(actionTriggered[0], "The search listener should be triggered by fireSearchEvent");
    }
}

