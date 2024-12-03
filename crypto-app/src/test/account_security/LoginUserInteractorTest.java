package account_security;

import application.interactors.login_user.LoginUserInteractor;
import domain.entities.User;
import domain.entities.Transaction;
import domain.entities.PortfolioEntry;
import infrastructure.interfaces.UserApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginUserInteractorTest {

    private UserApi mockUserApi;
    private LoginUserInteractor interactor;

    @BeforeEach
    void setUp() {
        mockUserApi = Mockito.mock(UserApi.class);
        interactor = new LoginUserInteractor(mockUserApi);
    }

    @Test
    void execute_successfulLogin_shouldReturnUser() throws IOException {
        String userId = "user123";
        String email = "test@example.com";
        String password = "securePassword";

        double cashBalance = 1000.0;
        List<Transaction> transactions = Arrays.asList(
                new Transaction("BTC", 1.0, 50000.0, new Date(), "BUY"),
                new Transaction("ETH", 2.0, 2000.0, new Date(), "SELL")
        );
        List<PortfolioEntry> portfolioEntries = Arrays.asList(
                new PortfolioEntry("BTC", 1.0),
                new PortfolioEntry("ETH", 2.0)
        );

        when(mockUserApi.loginUser(email, password)).thenReturn(userId);
        when(mockUserApi.getCashReserves(userId)).thenReturn(cashBalance);
        when(mockUserApi.getTransactions(userId)).thenReturn(transactions);
        when(mockUserApi.getPortfolioEntries(userId)).thenReturn(portfolioEntries);

        User user = interactor.execute(email, password);

        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(email, user.getUsername());
        assertEquals(cashBalance, user.getCashBalance());
        assertEquals(transactions, user.getTransactions());
        assertEquals(portfolioEntries, user.getPortfolio());

        verify(mockUserApi).loginUser(email, password);
        verify(mockUserApi).getCashReserves(userId);
        verify(mockUserApi).getTransactions(userId);
        verify(mockUserApi).getPortfolioEntries(userId);
    }

    @Test
    void execute_loginFails_shouldThrowIOException() throws IOException {
        String email = "test@example.com";
        String password = "wrongPassword";

        when(mockUserApi.loginUser(email, password)).thenThrow(new IOException("Login failed"));

        IOException exception = assertThrows(IOException.class, () ->
                interactor.execute(email, password));

        assertEquals("Login failed", exception.getMessage());

        verify(mockUserApi).loginUser(email, password);
        verifyNoMoreInteractions(mockUserApi);
    }

    @Test
    void execute_userHasNoTransactionsAndPortfolio_shouldReturnUserWithEmptyLists() throws IOException {
        String userId = "user456";
        String email = "newuser@example.com";
        String password = "newPassword";

        double cashBalance = 0.0;

        when(mockUserApi.loginUser(email, password)).thenReturn(userId);
        when(mockUserApi.getCashReserves(userId)).thenReturn(cashBalance);
        when(mockUserApi.getTransactions(userId)).thenReturn(Collections.emptyList());
        when(mockUserApi.getPortfolioEntries(userId)).thenReturn(Collections.emptyList());

        User user = interactor.execute(email, password);

        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(email, user.getUsername());
        assertEquals(cashBalance, user.getCashBalance());
        assertTrue(user.getTransactions().isEmpty());
        assertTrue(user.getPortfolio().isEmpty());

        verify(mockUserApi).loginUser(email, password);
        verify(mockUserApi).getCashReserves(userId);
        verify(mockUserApi).getTransactions(userId);
        verify(mockUserApi).getPortfolioEntries(userId);
    }
}