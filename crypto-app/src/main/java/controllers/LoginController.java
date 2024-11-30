package controllers;

import entities.User;
import services.UserService;
import services.INewsService;
import views.LoginView;
import views.MainView;

import javax.swing.*;

public class LoginController {

    private final UserService userService;
    private final INewsService newsService;
    private final JFrame frame;

    public LoginController(UserService userService, INewsService newsService, JFrame frame) {
        this.userService = userService;
        this.newsService = newsService;
        this.frame = frame;
    }

    public void start() {
        LoginView loginView = new LoginView();

        loginView.addLoginListener(e -> handleLogin(loginView));
        loginView.addRegisterListener(e -> handleRegister(loginView));
        loginView.addResetPasswordListener(e -> handleResetPassword(loginView));

        frame.setContentPane(loginView);
        frame.revalidate();
    }

    private void handleLogin(LoginView loginView) {
        String email = loginView.getEmail();
        String password = loginView.getPassword();
        try {
            User currentUser = userService.loginUser(email, password);
            showMainView(currentUser);
        } catch (Exception e) {
            loginView.setStatusMessage("Error: " + e.getMessage());
        }
    }

    private void handleRegister(LoginView loginView) {
        String email = loginView.getEmail();
        String password = loginView.getPassword();
        try {
            User currentUser = userService.registerUser("New User", email, password, "What is your favorite color?", "Blue");
            showMainView(currentUser);
        } catch (Exception e) {
            loginView.setStatusMessage("Error: " + e.getMessage());
        }
    }

    private void handleResetPassword(LoginView loginView) {
        String email = loginView.getEmail();
        try {
            userService.resetPassword(email);
            loginView.setStatusMessage("Password reset email sent successfully!");
        } catch (Exception e) {
            loginView.setStatusMessage("Error: " + e.getMessage());
        }
    }

    private void showMainView(User currentUser) {
        MainController mainController = new MainController(userService, newsService, currentUser, frame);
        mainController.start();
    }
}
