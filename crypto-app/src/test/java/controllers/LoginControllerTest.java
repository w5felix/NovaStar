package controllers;

import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.INewsService;
import services.UserService;
import views.LoginView;

import javax.swing.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private UserService userServiceMock;
    private INewsService newsServiceMock;
    private JFrame frameMock;
    private LoginView loginViewMock;
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        userServiceMock = mock(UserService.class);
        newsServiceMock = mock(INewsService.class);
        frameMock = mock(JFrame.class);
        loginViewMock = mock(LoginView.class);

        // Initialize the LoginController
        loginController = new LoginController(userServiceMock, newsServiceMock, frameMock);
    }

    @Test
    void testHandleLogin_SuccessfulLogin() throws Exception {
        // Arrange
        User mockUser = new User("123", "testUser", 1000.0, null, null);
        when(loginViewMock.getEmail()).thenReturn("test@example.com");
        when(loginViewMock.getPassword()).thenReturn("password123");
        when(userServiceMock.loginUser("test@example.com", "password123")).thenReturn(mockUser);

        // Act
        loginController.start(); // Initializes the view
        loginController.handleLogin(loginViewMock);

        // Assert
        verify(userServiceMock).loginUser("test@example.com", "password123");
        verify(loginViewMock, never()).setStatusMessage(anyString()); // No error message
    }

    @Test
    void testHandleLogin_FailedLogin() throws Exception {
        // Arrange
        when(loginViewMock.getEmail()).thenReturn("test@example.com");
        when(loginViewMock.getPassword()).thenReturn("wrongpassword");
        when(userServiceMock.loginUser(anyString(), anyString())).thenThrow(new Exception("Invalid credentials"));

        // Act
        loginController.start(); // Initializes the view
        loginController.handleLogin(loginViewMock);

        // Assert
        verify(userServiceMock).loginUser("test@example.com", "wrongpassword");
        verify(loginViewMock).setStatusMessage("Error: Invalid credentials");
    }

    @Test
    void testHandleRegister_SuccessfulRegistration() throws Exception {
        // Arrange
        User mockUser = new User("123", "New User", 1000.0, null, null);
        when(loginViewMock.getEmail()).thenReturn("newuser@example.com");
        when(loginViewMock.getPassword()).thenReturn("securepassword");
        when(userServiceMock.registerUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockUser);

        // Act
        loginController.start(); // Initializes the view
        loginController.handleRegister(loginViewMock);

        // Assert
        verify(userServiceMock).registerUser(eq("New User"), eq("newuser@example.com"), eq("securepassword"),
                eq("What is your favorite color?"), eq("Blue"));
        verify(loginViewMock, never()).setStatusMessage(anyString()); // No error message
    }

    @Test
    void testHandleRegister_FailedRegistration() throws Exception {
        // Arrange
        when(loginViewMock.getEmail()).thenReturn("invalidemail@example.com");
        when(loginViewMock.getPassword()).thenReturn("weakpassword");
        when(userServiceMock.registerUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new Exception("Email already exists"));

        // Act
        loginController.start(); // Initializes the view
        loginController.handleRegister(loginViewMock);

        // Assert
        verify(userServiceMock).registerUser(eq("New User"), eq("invalidemail@example.com"), eq("weakpassword"),
                eq("What is your favorite color?"), eq("Blue"));
        verify(loginViewMock).setStatusMessage("Error: Email already exists");
    }

    @Test
    void testHandleResetPassword_SuccessfulReset() throws Exception {
        // Arrange
        when(loginViewMock.getEmail()).thenReturn("resetuser@example.com");

        // Act
        loginController.start(); // Initializes the view
        loginController.handleResetPassword(loginViewMock);

        // Assert
        verify(userServiceMock).resetPassword("resetuser@example.com");
        verify(loginViewMock).setStatusMessage("Password reset email sent successfully!");
    }

    @Test
    void testHandleResetPassword_FailedReset() throws Exception {
        // Arrange
        when(loginViewMock.getEmail()).thenReturn("nonexistent@example.com");
        doThrow(new Exception("User not found")).when(userServiceMock).resetPassword(anyString());

        // Act
        loginController.start(); // Initializes the view
        loginController.handleResetPassword(loginViewMock);

        // Assert
        verify(userServiceMock).resetPassword("nonexistent@example.com");
        verify(loginViewMock).setStatusMessage("Error: User not found");
    }
}
