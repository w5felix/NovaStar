package services;

import adapters.BlockchainServiceAdapter;
import entities.User;
import interfaces.UserApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private UserApi userApiMock; // Assuming UserApi is an interface

    @BeforeEach
    void setUp() {
        userApiMock = mock(UserApi.class); // Create a mock UserApi
        userService = new UserService(userApiMock, new BlockchainServiceAdapter()); // BlockchainServiceAdapter is used as is if it doesn't need mocking
    }

    @Test
    void testUserLogin() throws Exception {
        // Setup conditions of the mock
        when(userApiMock.loginUser("user@example.com", "password")).thenReturn("12345");

        // Execute the method to test
        User result = userService.loginUser("user@example.com", "password");

        // Assert the results
        assertNotNull(result);
        assertEquals("12345", result.getUserId());
    }
}
