package controllers;

import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.INewsService;
import services.UserService;
import views.*;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainControllerTest {

    private UserService mockUserService;
    private INewsService mockNewsService;
    private User mockUser;
    private JFrame mockFrame;
    private MainController mainController;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockUserService = Mockito.mock(UserService.class);
        mockNewsService = Mockito.mock(INewsService.class);
        mockUser = new User("123", "testUser", 1000.00, new ArrayList<>(), new ArrayList<>());
        mockFrame = Mockito.mock(JFrame.class);

        // Initialize MainController
        mainController = new MainController(mockUserService, mockNewsService, mockUser, mockFrame);
    }

    @Test
    void testStartSetsMainViewAsContentPane() {
        // Arrange
        MainView mockMainView = Mockito.spy(new MainView());

        // Act
        mainController.start();

        // Assert
        verify(mockFrame).setContentPane(any(MainView.class));
        verify(mockFrame).revalidate();
    }

    @Test
    void testUpdateHeaderValuesDisplaysCorrectPortfolioValue() throws Exception {
        // Arrange
        MainView mockMainView = spy(new MainView());
        when(mockUserService.calculatePortfolioValue(mockUser)).thenReturn(1500.0);

        // Act
        mainController.start();
        mainController.updateHeaderValues(mockMainView);

        // Assert
        verify(mockMainView).updateCashReserves(mockUser.getCashBalance());
        verify(mockMainView).updatePortfolioValue(1500.0);
    }

    @Test
    void testSetupTabsAddsCorrectPanels() {
        // Arrange
        MainView mockMainView = spy(new MainView());

        // Act
        mainController.start();
        mainController.setupTabs(mockMainView);

        // Assert
        JTabbedPane tabbedPane = mockMainView.getTabbedPane();
        assertEquals(4, tabbedPane.getTabCount()); // Portfolio, Transactions, Crypto Prices, News Search
    }

    @Test
    void testRefreshDataHandlesExceptionsGracefully() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Error refreshing data"))
                .when(mockUserService).calculatePortfolioValue(mockUser);

        // Act & Assert
        assertDoesNotThrow(() -> mainController.refreshData());
    }
}
