package application.interactors.reset_password;

import infrastructure.interfaces.UserApi;

import java.io.IOException;

/**
 * Interactor for handling password reset.
 */
public class ResetPasswordInteractor {

    private final UserApi userApi;

    public ResetPasswordInteractor(UserApi userApi) {
        this.userApi = userApi;
    }

    /**
     * Executes the password reset process.
     *
     * @param email The user's email.
     * @throws IOException If the operation fails.
     */
    public void execute(String email) throws IOException {
        userApi.resetPassword(email);
    }
}
