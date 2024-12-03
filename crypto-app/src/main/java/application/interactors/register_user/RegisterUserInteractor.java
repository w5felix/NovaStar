package application.interactors.register_user;

import domain.entities.User;
import infrastructure.interfaces.UserApi;

import java.io.IOException;

/**
 * Interactor for handling user registration.
 */
public class RegisterUserInteractor {

    private final UserApi userApi;

    public RegisterUserInteractor(UserApi userApi) {
        this.userApi = userApi;
    }

    /**
     * Executes the registration process.
     *
     * @param username             The user's username.
     * @param email                The user's email.
     * @param password             The user's password.
     * @param securityQuestion     The user's security question.
     * @param securityAnswer       The user's security question answer.
     * @return The registered User object.
     * @throws IOException If registration fails.
     */
    public User execute(String username, String email, String password,
                        String securityQuestion, String securityAnswer) throws IOException {
        String userId = userApi.registerUser(username, email, password, securityQuestion, securityAnswer);
        double cashBalance = userApi.getCashReserves(userId);
        return new User(userId, username, cashBalance,
                        userApi.getTransactions(userId),
                        userApi.getPortfolioEntries(userId));
    }
}
