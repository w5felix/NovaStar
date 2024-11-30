package adapters;

import api.FireBaseAPIClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirebaseServiceAdapterTest {

    private FirebaseServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FirebaseServiceAdapter();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginUser() throws IOException {
        try (MockedStatic<FireBaseAPIClient> mockedApi = mockStatic(FireBaseAPIClient.class)) {
            mockedApi.when(() -> FireBaseAPIClient.loginUser("test@example.com", "password"))
                    .thenReturn("Success");

            assertEquals("Success", adapter.loginUser("test@example.com", "password"));
        }
    }

    @Test
    void testRegisterUser() throws IOException {
        try (MockedStatic<FireBaseAPIClient> mockedApi = mockStatic(FireBaseAPIClient.class)) {
            mockedApi.when(() -> FireBaseAPIClient.registerUser("user", "user@example.com", "password", "SQ?", "Answer"))
                    .thenReturn("UserID123");

            assertEquals("UserID123", adapter.registerUser("user", "user@example.com", "password", "SQ?", "Answer"));
        }
    }

    @Test
    void testAddCash() throws IOException {
        try (MockedStatic<FireBaseAPIClient> mockedApi = mockStatic(FireBaseAPIClient.class)) {
            mockedApi.when(() -> FireBaseAPIClient.addCash("UserID123", 100.0))
                    .thenAnswer(invocation -> null);

            assertDoesNotThrow(() -> adapter.addCash("UserID123", 100.0));
        }
    }

    // Additional tests for each method like getTransactions, getPortfolioEntries, etc.

}

