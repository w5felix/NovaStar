package main;

import controllers.LoginController;
import services.UserService;
import adapters.FirebaseServiceAdapter;
import adapters.BlockchainServiceAdapter;
import services.NewsService;
import services.INewsService;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize services
            UserService userService = new UserService(
                    new FirebaseServiceAdapter(),
                    new BlockchainServiceAdapter()
            );
            INewsService newsService = new NewsService();

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
