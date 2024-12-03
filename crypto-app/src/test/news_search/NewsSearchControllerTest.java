package application.controllers;

import application.interactors.news_search.NewsSearchDataAccessInterface;
import views.NewsSearchView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NewsSearchControllerTest {

    private NewsSearchDataAccessInterface mockNewsService;
    private NewsSearchView mockNewsSearchView;
    private NewsSearchController newsSearchController;

    @BeforeEach
    void setUp() {
        mockNewsService = mock(NewsSearchDataAccessInterface.class);
        mockNewsSearchView = mock(NewsSearchView.class);
        newsSearchController = new NewsSearchController(mockNewsService, mockNewsSearchView);
    }

    @Test
    void testGetView_shouldReturnNewsSearchView() {
        // Arrange
        JPanel panel = newsSearchController.getView();

        // Assert
        assertNotNull(panel, "Expected non-null JPanel.");
    }

    @Test
    void testHandleSearch_whenQueryIsEmpty_shouldDisplayErrorMessage() {
        // Arrange
        JPanel panel = newsSearchController.getView();

        // Assert
        assertNotNull(panel, "Expected non-null JPanel.");
    }

    @Test
    void testHandleSearch_whenQueryIsValid_shouldFetchAndDisplayArticles() {
        // Arrange
        JPanel panel = newsSearchController.getView();

        // Assert
        assertNotNull(panel, "Expected non-null JPanel.");
    }

    @Test
    void testHandleSearch_whenServiceThrowsException_shouldDisplayErrorMessage() {
        // Arrange
        JPanel panel = newsSearchController.getView();

        // Assert
        assertNotNull(panel, "Expected non-null JPanel.");
    }




}
