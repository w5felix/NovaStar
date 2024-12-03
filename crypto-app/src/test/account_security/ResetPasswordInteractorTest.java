package account_security;

import application.interactors.reset_password.ResetPasswordInteractor;
import infrastructure.interfaces.UserApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResetPasswordInteractorTest {

    private UserApi mockUserApi;
    private ResetPasswordInteractor interactor;

    @BeforeEach
    void setUp() {
        mockUserApi = Mockito.mock(UserApi.class);
        interactor = new ResetPasswordInteractor(mockUserApi);
    }

    @Test
    void execute_successfulResetPassword_shouldInvokeApi() throws IOException {
        String email = "test@example.com";

        interactor.execute(email);

        verify(mockUserApi).resetPassword(email);
        verifyNoMoreInteractions(mockUserApi);
    }

    @Test
    void execute_resetPasswordFails_shouldThrowIOException() throws IOException {
        String email = "test@example.com";

        doThrow(new IOException("Reset password failed")).when(mockUserApi).resetPassword(email);

        IOException exception = assertThrows(IOException.class, () ->
                interactor.execute(email));

        assertEquals("Reset password failed", exception.getMessage());

        verify(mockUserApi).resetPassword(email);
        verifyNoMoreInteractions(mockUserApi);
    }
}