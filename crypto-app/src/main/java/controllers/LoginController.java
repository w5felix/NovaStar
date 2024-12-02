package controllers;

import javax.swing.JFrame;

import entities.User;
import interactors.UserService;
import news_search.NewsSearchDataAccessInterface;
import views.LoginView;

public class LoginController {

    public static final String ERROR = "Error: ";

    private final UserService userService;
    private final NewsSearchDataAccessInterface newsService;
    private final JFrame frame;

    public LoginController(UserService userService, NewsSearchDataAccessInterface newsService, JFrame frame) {
        this.userService = userService;
        this.newsService = newsService;
        this.frame = frame;
    }

    /**
     * Login view for starting.
     */
    public void start() {
        final LoginView loginView = new LoginView();

        loginView.addLoginListener(actionEvent -> handleLogin(loginView));
        loginView.addRegisterListener(actionEvent -> handleRegister(loginView));
        loginView.addResetPasswordListener(actionEvent -> handleResetPassword(loginView));

        frame.setContentPane(loginView);
        frame.revalidate();
    }

    private void handleLogin(LoginView loginView) {
        final String email = loginView.getEmail();
        final String password = loginView.getPassword();
        try {
            final User currentUser = userService.loginUser(email, password);
            showMainView(currentUser);
        }
        catch (Exception exception) {
            loginView.setStatusMessage(ERROR + exception.getMessage());
        }
    }

    private void handleRegister(LoginView loginView) {
        final String email = loginView.getEmail();
        final String password = loginView.getPassword();
        try {
            final User currentUser = userService.registerUser(
                    "New User", email, password,
                    "What is your favorite color?", "Blue");
            showMainView(currentUser);
        }
        catch (Exception exception) {
            loginView.setStatusMessage(ERROR + exception.getMessage());
        }
    }

    private void handleResetPassword(LoginView loginView) {
        final String email = loginView.getEmail();
        try {
            userService.resetPassword(email);
            loginView.setStatusMessage("Password reset email sent successfully!");
        }
        catch (Exception exception) {
            loginView.setStatusMessage(ERROR + exception.getMessage());
        }
    }

    private void showMainView(User currentUser) {
        final MainController mainController = new MainController(userService, newsService, currentUser, frame);
        mainController.start();
    }
}
