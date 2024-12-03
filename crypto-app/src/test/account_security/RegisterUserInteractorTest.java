package account_security;

import application.interactors.register_user.RegisterUserInteractor;
import domain.entities.User;
import domain.entities.Transaction;
import domain.entities.PortfolioEntry;
import infrastructure.interfaces.UserApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserInteractorTest {

    private UserApi mockUserApi;
    private RegisterUserInteractor interactor;

    @BeforeEach
    void setUp() {
        mockUserApi = Mockito.mock(UserApi.class);
        interactor = new RegisterUserInteractor(mockUserApi);
    }

    @Test
    void execute_successfulRegistration_shouldReturnUser() throws IOException {
        String userId = "user123";
        String username = "testUser";
        String email = "test@example.com";
        String password = "securePassword";
        String securityQuestion = "What is your pet's name?";
        String securityAnswer = "Buddy";

        double cashBalance = 1000.0;
        List<Transaction> transactions = List.of(
                new Transaction("BTC", 1.0, 50000.0, new Date(), "BUY"),
                new Transaction("ETH", 2.0, 2000.0, new Date(), "SELL")
        );
        List<PortfolioEntry> portfolioEntries = List.of(
                new PortfolioEntry("BTC", 1.0),
                new PortfolioEntry("ETH", 2.0)
        );

        when(mockUserApi.registerUser(username, email, password, securityQuestion, securityAnswer)).thenReturn(userId);
        when(mockUserApi.getCashReserves(userId)).thenReturn(cashBalance);
        when(mockUserApi.getTransactions(userId)).thenReturn(transactions);
        when(mockUserApi.getPortfolioEntries(userId)).thenReturn(portfolioEntries);

        User user = interactor.execute(username, email, password, securityQuestion, securityAnswer);

        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(username, user.getUsername());
        assertEquals(cashBalance, user.getCashBalance());
        assertEquals(transactions, user.getTransactions());
        assertEquals(portfolioEntries, user.getPortfolio());

        verify(mockUserApi).registerUser(username, email, password, securityQuestion, securityAnswer);
        verify(mockUserApi).getCashReserves(userId);
        verify(mockUserApi).getTransactions(userId);
        verify(mockUserApi).getPortfolioEntries(userId);
    }

    @Test
    void execute_registrationFails_shouldThrowIOException() throws IOException {
        String username = "testUser";
        String email = "test@example.com";
        String password = "securePassword";
        String securityQuestion = "What is your pet's name?";
        String securityAnswer = "Buddy";

        when(mockUserApi.registerUser(username, email, password, securityQuestion, securityAnswer))
                .thenThrow(new IOException("Registration failed"));

        IOException exception = assertThrows(IOException.class, () ->
                interactor.execute(username, email, password, securityQuestion, securityAnswer));

        assertEquals("Registration failed", exception.getMessage());

        verify(mockUserApi).registerUser(username, email, password, securityQuestion, securityAnswer);
        verifyNoMoreInteractions(mockUserApi);
    }

    @Test
    void execute_userHasNoTransactionsAndPortfolio_shouldReturnUserWithEmptyLists() throws IOException {
        String userId = "user456";
        String username = "newUser";
        String email = "newuser@example.com";
        String password = "newPassword";
        String securityQuestion = "What is your favorite color?";
        String securityAnswer = "Blue";

        double cashBalance = 0.0;

        when(mockUserApi.registerUser(username, email, password, securityQuestion, securityAnswer)).thenReturn(userId);
        when(mockUserApi.getCashReserves(userId)).thenReturn(cashBalance);
        when(mockUserApi.getTransactions(userId)).thenReturn(Collections.emptyList());
        when(mockUserApi.getPortfolioEntries(userId)).thenReturn(Collections.emptyList());

        User user = interactor.execute(username, email, password, securityQuestion, securityAnswer);

        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(username, user.getUsername());
        assertEquals(cashBalance, user.getCashBalance());
        assertTrue(user.getTransactions().isEmpty());
        assertTrue(user.getPortfolio().isEmpty());

        verify(mockUserApi).registerUser(username, email, password, securityQuestion, securityAnswer);
        verify(mockUserApi).getCashReserves(userId);
        verify(mockUserApi).getTransactions(userId);
        verify(mockUserApi).getPortfolioEntries(userId);
    }
}