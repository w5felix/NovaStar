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
        // 使用Mockito创建Mock对象
        mockUserApi = Mockito.mock(UserApi.class);
        // 初始化LoginUserInteractor
        interactor = new LoginUserInteractor(mockUserApi);
    }

    @Test
    void execute_successfulLogin_shouldReturnUser() throws IOException {
        // Arrange: 模拟用户登录成功
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

        // Mock UserApi 的方法返回值
        when(mockUserApi.loginUser(email, password)).thenReturn(userId);
        when(mockUserApi.getCashReserves(userId)).thenReturn(cashBalance);
        when(mockUserApi.getTransactions(userId)).thenReturn(transactions);
        when(mockUserApi.getPortfolioEntries(userId)).thenReturn(portfolioEntries);

        // Act: 调用 LoginUserInteractor 的 execute 方法
        User user = interactor.execute(email, password);

        // Assert: 验证返回结果
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(email, user.getUsername());
        assertEquals(cashBalance, user.getCashBalance());
        assertEquals(transactions, user.getTransactions());
        assertEquals(portfolioEntries, user.getPortfolio());

        // 验证Mock方法的调用
        verify(mockUserApi).loginUser(email, password);
        verify(mockUserApi).getCashReserves(userId);
        verify(mockUserApi).getTransactions(userId);
        verify(mockUserApi).getPortfolioEntries(userId);
    }

    @Test
    void execute_loginFails_shouldThrowIOException() throws IOException {
        // Arrange: 模拟登录失败的场景
        String email = "test@example.com";
        String password = "wrongPassword";

        // Mock loginUser 抛出异常
        when(mockUserApi.loginUser(email, password)).thenThrow(new IOException("Login failed"));

        // Act & Assert: 验证抛出IOException
        IOException exception = assertThrows(IOException.class, () ->
                interactor.execute(email, password));

        assertEquals("Login failed", exception.getMessage());

        // 验证Mock方法的调用
        verify(mockUserApi).loginUser(email, password);
        verifyNoMoreInteractions(mockUserApi);
    }

    @Test
    void execute_userHasNoTransactionsAndPortfolio_shouldReturnUserWithEmptyLists() throws IOException {
        // Arrange: 模拟用户没有交易和投资组合
        String userId = "user456";
        String email = "newuser@example.com";
        String password = "newPassword";

        double cashBalance = 0.0;

        // Mock UserApi 的方法返回值
        when(mockUserApi.loginUser(email, password)).thenReturn(userId);
        when(mockUserApi.getCashReserves(userId)).thenReturn(cashBalance);
        when(mockUserApi.getTransactions(userId)).thenReturn(Collections.emptyList());
        when(mockUserApi.getPortfolioEntries(userId)).thenReturn(Collections.emptyList());

        // Act: 调用 LoginUserInteractor 的 execute 方法
        User user = interactor.execute(email, password);

        // Assert: 验证返回结果
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(email, user.getUsername());
        assertEquals(cashBalance, user.getCashBalance());
        assertTrue(user.getTransactions().isEmpty());
        assertTrue(user.getPortfolio().isEmpty());

        // 验证Mock方法的调用
        verify(mockUserApi).loginUser(email, password);
        verify(mockUserApi).getCashReserves(userId);
        verify(mockUserApi).getTransactions(userId);
        verify(mockUserApi).getPortfolioEntries(userId);
    }
}