package main;

import controllers.LoginController;
import interactors.UserService;
import interface_adapters.FirebaseServiceAdapter;
import interface_adapters.BlockchainServiceAdapter;
import news_search.NewsSearchInteractor;
import news_search.NewsSearchDataAccessInterface;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize services
            UserService userService = new UserService(
                    new FirebaseServiceAdapter(),
                    new BlockchainServiceAdapter()
            );
            NewsSearchDataAccessInterface newsService = new NewsSearchInteractor();

            // Set up the main application frame
            JFrame frame = new JFrame("Crypto Portfolio Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);

            // Start the login process
            LoginController loginController = new LoginController(userService, newsService, frame);
            loginController.start();

            // Make the frame visible
            frame.setVisible(true);
        });
    }
}
