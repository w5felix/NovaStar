package controllers;

import api.BlockChainAPIClient;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import services.UserService;
import views.CryptoPricesView;

import javax.swing.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CryptoPricesControllerTest {

    private UserService mockUserService;
    private User mockUser;
    private CryptoPricesView mockCryptoPricesView;
    private PortfolioController mockPortfolioController;
    private TransactionsController mockTransactionsController;
    private CryptoPricesController cryptoPricesController;

    @BeforeEach
    public void setUp() {
        mockUserService = mock(UserService.class);
        mockUser = mock(User.class);
        mockCryptoPricesView = mock(CryptoPricesView.class);
        mockPortfolioController = mock(PortfolioController.class);
        mockTransactionsController = mock(TransactionsController.class);

        cryptoPricesController = new CryptoPricesController(
                mockUserService, mockUser, mockCryptoPricesView,
                mockPortfolioController, mockTransactionsController
        );
    }

    @Test
    void testHandleBuy() throws Exception {
        BlockChainAPIClient.CryptoInfo crypto = new BlockChainAPIClient.CryptoInfo("Bitcoin", "BTC-USD", 50000.0f, 2.5f);

        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(any(), any())).thenReturn("2");
            mockedJOptionPane.when(() -> JOptionPane.showMessageDialog(any(), any())).thenAnswer(invocation -> null);

            cryptoPricesController.handleBuy(crypto);

            verify(mockUserService, times(1)).buyCrypto(mockUser, "Bitcoin", 2.0);
        }
    }

    @Test
    void testHandleSell() throws Exception {
        BlockChainAPIClient.CryptoInfo crypto = new BlockChainAPIClient.CryptoInfo("Bitcoin", "BTC-USD", 50000.0f, 2.5f);

        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(any(), any())).thenReturn("1");
            mockedJOptionPane.when(() -> JOptionPane.showMessageDialog(any(), any())).thenAnswer(invocation -> null);

            cryptoPricesController.handleSell(crypto);

            verify(mockUserService, times(1)).sellCrypto(mockUser, "Bitcoin", 1.0);
        }
    }


    @Test
    public void testRefreshData() throws Exception {
        // Simulate refreshing data successfully
        doNothing().when(mockPortfolioController).refresh();
        doNothing().when(mockTransactionsController).refresh();

        cryptoPricesController.refreshData();

        // Verify that both controllers are refreshed
        verify(mockPortfolioController).refresh();
        verify(mockTransactionsController).refresh();
    }
}