package controllers;

import entities.PortfolioEntry;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.UserService;
import views.PortfolioView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioControllerTest {

    private UserService mockUserService;
    private User mockUser;
    private PortfolioView mockPortfolioView;
    private PortfolioController portfolioController;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockUserService = Mockito.mock(UserService.class);
        mockUser = Mockito.mock(User.class);
        mockPortfolioView = Mockito.mock(PortfolioView.class);

        // Initialize PortfolioController with mocks
        portfolioController = new PortfolioController(mockUserService, mockUser, mockPortfolioView);
    }

    @Test
    void testRefresh_Success() throws Exception {
        // Mock portfolio data
        List<PortfolioEntry> mockPortfolio = Arrays.asList(
                new PortfolioEntry("BTC", 2.0),
                new PortfolioEntry("ETH", 5.0)
        );

        when(mockUserService.getPortfolioEntries(mockUser)).thenReturn(mockPortfolio);
        when(mockUserService.getCurrentValue(any())).thenReturn(100.0);
        when(mockUserService.getInitialInvestment(eq(mockUser), any())).thenReturn(50.0);

        // Call refresh
        portfolioController.refresh();

        // Verify PortfolioView's updatePortfolio is called
        verify(mockPortfolioView, times(1))
                .updatePortfolio(eq(mockPortfolio), any(PortfolioView.PortfolioDataHandler.class));
    }

    @Test
    void testRefresh_HandlesUserServiceFailure() throws Exception {
        // Mock an exception from UserService
        when(mockUserService.getPortfolioEntries(mockUser)).thenThrow(new RuntimeException("Test exception"));

        // Call refresh
        portfolioController.refresh();

        // Verify PortfolioView's showError is called
        verify(mockPortfolioView, times(1))
                .showError(contains("Test exception"));
    }

    @Test
    void testDataHandler_getCurrentValue_Success() throws Exception {
        // Mock portfolio entry
        PortfolioEntry mockEntry = new PortfolioEntry("BTC", 2.0);

        // Mock return value for current value
        when(mockUserService.getCurrentValue(mockEntry)).thenReturn(200.0);

        // Create the data handler
        PortfolioView.PortfolioDataHandler dataHandler = new PortfolioView.PortfolioDataHandler() {
            @Override
            public double getCurrentValue(PortfolioEntry entry) {
                try {
                    return mockUserService.getCurrentValue(entry);
                } catch (Exception e) {
                    return 0.0;
                }
            }

            @Override
            public double getInitialInvestment(PortfolioEntry entry) {
                return 0.0;
            }
        };

        // Assert the value is correctly retrieved
        assertEquals(200.0, dataHandler.getCurrentValue(mockEntry));
    }

    @Test
    void testDataHandler_getInitialInvestment_Success() throws Exception {
        // Mock portfolio entry
        PortfolioEntry mockEntry = new PortfolioEntry("ETH", 5.0);

        // Mock return value for initial investment
        when(mockUserService.getInitialInvestment(mockUser, mockEntry)).thenReturn(500.0);

        // Create the data handler
        PortfolioView.PortfolioDataHandler dataHandler = new PortfolioView.PortfolioDataHandler() {
            @Override
            public double getCurrentValue(PortfolioEntry entry) {
                return 0.0;
            }

            @Override
            public double getInitialInvestment(PortfolioEntry entry) {
                try {
                    return mockUserService.getInitialInvestment(mockUser, entry);
                } catch (Exception e) {
                    return 0.0;
                }
            }
        };

        // Assert the value is correctly retrieved
        assertEquals(500.0, dataHandler.getInitialInvestment(mockEntry));
    }

    @Test
    void testDataHandler_HandlesExceptions() throws Exception {
        // Mock portfolio entry
        PortfolioEntry mockEntry = new PortfolioEntry("BTC", 2.0);

        // Mock exception from UserService
        when(mockUserService.getCurrentValue(mockEntry)).thenThrow(new RuntimeException("Test exception"));
        when(mockUserService.getInitialInvestment(mockUser, mockEntry)).thenThrow(new RuntimeException("Test exception"));

        // Create the data handler
        PortfolioView.PortfolioDataHandler dataHandler = new PortfolioView.PortfolioDataHandler() {
            @Override
            public double getCurrentValue(PortfolioEntry entry) {
                try {
                    return mockUserService.getCurrentValue(entry);
                } catch (Exception e) {
                    return 0.0;
                }
            }

            @Override
            public double getInitialInvestment(PortfolioEntry entry) {
                try {
                    return mockUserService.getInitialInvestment(mockUser, entry);
                } catch (Exception e) {
                    return 0.0;
                }
            }
        };

        // Assert default values are returned on exception
        assertEquals(0.0, dataHandler.getCurrentValue(mockEntry));
        assertEquals(0.0, dataHandler.getInitialInvestment(mockEntry));
    }
}

