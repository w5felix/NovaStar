package application.interactors.login_user;

import domain.entities.User;
import infrastructure.interfaces.UserApi;

import java.io.IOException;

/**
 * Interactor for handling user login.
 */
public class LoginUserInteractor {

    private final UserApi userApi;

    public LoginUserInteractor(UserApi userApi) {
        this.userApi = userApi;
    }

    /**
     * Executes the login process.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return The logged-in User object.
     * @throws IOException If login fails or data retrieval encounters an issue.
     */
    public User execute(String email, String password) throws IOException {
        String userId = userApi.loginUser(email, password);
        double cashBalance = userApi.getCashReserves(userId);
        return new User(userId, email, cashBalance,
                        userApi.getTransactions(userId),
                        userApi.getPortfolioEntries(userId));
    }
}
