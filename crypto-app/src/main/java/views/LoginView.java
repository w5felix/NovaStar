package views;

import api.FireBaseAPIClient;
import entities.User;
import interface_adapterss.UserService;
import interface_adapterss.ViewModel;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {

    private UserService userService = new UserService(
            new interface_adapterss.FirebaseServiceAdapter(), // Pass the FirebaseServiceAdapter
            new interface_adapterss.BlockchainServiceAdapter());

    private final Font font = new Font("Times New Roman", Font.PLAIN, 25);

    private final ViewModel viewModel;

    private JButton loginButton = new JButton("Login");

    private JButton createAnAccount = new JButton("create an account");

    private JButton reset = new JButton("reset");

    private JTextField nameField = new JTextField();

    private JPasswordField passwordField = new JPasswordField();

    public LoginView(ViewModel viewModel) {

        this.viewModel = viewModel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel label1 = new JLabel("Crypto calculator and currency conversion");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setFont(font);
        add(label1, gbc);
        JLabel label2 = new JLabel("Welcome");
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setFont(font);
        add(label2, gbc);
        JLabel label3 = new JLabel("Login the App");
        label3.setHorizontalAlignment(SwingConstants.CENTER);
        label3.setFont(font);
        add(label3, gbc);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label4 = new JLabel("email address");
        label4.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label4.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label4);
        nameField.setMaximumSize(new Dimension(600, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nameField);
        JLabel label5 = new JLabel("Password");
        label5.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label5.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label5);
        passwordField.setMaximumSize(new Dimension(600, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(passwordField);
        add(panel, gbc);

        JPanel panel_1 = new JPanel();
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel_1.add(loginButton);
        add(panel_1, gbc);

        JPanel panel_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_2.add(new JLabel("New to the APP?"));
        panel_2.add(createAnAccount);
        add(panel_2, gbc);

        JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_3.add(new JLabel("Forget password? Need to reset?"));
        panel_3.add(reset);
        add(panel_3, gbc);

        loginButton.addActionListener(e -> {
            try{
/*                api.FireBaseAPIClient.checkUserByUserEmail(nameField.getText());
                api.FireBaseAPIClient.checkPasswordByUsernameOrEmail(passwordField.getPassword(),nameField.getText());
                User user = api.FireBaseAPIClient.getExistedUserFromUsernameOrEmail(nameField.getText());*/
                handleLogin(nameField.getText(), new String(passwordField.getPassword()), new JLabel(""));
                this.viewModel.setCurrentState(ViewModel.AFTER_LOGIN_VIEW);
                nameField.setText("");
                passwordField.setText("");
            } catch (Exception ex) {
                System.out.println("Username or password is incorrect, please try again");
                nameField.setText("");
                passwordField.setText("");
            }
        });

        createAnAccount.addActionListener(e -> {
            this.viewModel.setCurrentState(ViewModel.SIGNUP_VIEW);
            nameField.setText("");
            passwordField.setText("");
        });

        reset.addActionListener(e -> {
            this.viewModel.setCurrentState(ViewModel.FORGET_PASSWORD_VIEW);
            nameField.setText("");
            passwordField.setText("");
        });

    }

    private void handleLogin(String email, String password, JLabel statusLabel) {
        try {
            viewModel.setCurrentUser(userService.loginUser(email, password));
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}
