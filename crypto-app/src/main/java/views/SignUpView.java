package views;

import entities.User;
import interface_adapterss.UserService;
import interface_adapterss.ViewModel;

import javax.swing.*;
import java.awt.*;

public class SignUpView extends JPanel {

    private UserService userService = new UserService(
            new interface_adapterss.FirebaseServiceAdapter(), // Pass the FirebaseServiceAdapter
            new interface_adapterss.BlockchainServiceAdapter());

    private JButton signUpButton = new JButton("Sign Up");

    private JTextField nameField = new JTextField();

    private JTextField emailField = new JTextField();

    private JPasswordField passwordField = new JPasswordField();

    private JComboBox<String> verifyQuestionBox;

    private JPasswordField answerField = new JPasswordField();

    private final ViewModel viewmodel;

    public SignUpView(ViewModel viewmodel) {

        this.viewmodel = viewmodel;

        setLayout(new BorderLayout());
        setUpUI();
    }

    private void setUpUI() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Sign Up", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        JLabel nameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel emailLabel = new JLabel("Email Address:");
        JLabel verifyQuestionLabel = new JLabel("Security Question:");
        JLabel answerLabel = new JLabel("Answer:");

        String[] questions = {
                "What is your first pet's name?",
                "What's your primary school teacher's name?",
                "What's your nickname?",
                "Where were you born?"
        };
        verifyQuestionBox = new JComboBox<>(questions);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(verifyQuestionLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(verifyQuestionBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(answerLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(answerField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 80, 10));
        buttonPanel.add(signUpButton);
        add(buttonPanel, BorderLayout.SOUTH);

        signUpButton.addActionListener(e -> handleSignUp());
    }

    private void handleSignUp() {
        String username = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String securityQuestion = (String) verifyQuestionBox.getSelectedItem();
        String questionAnswer = new String(answerField.getPassword());

        User user = handleRegister(username, email, password, securityQuestion, questionAnswer);

        if (user != null){
            JOptionPane.showMessageDialog(this, "Registration Successful!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            viewmodel.setCurrentUser(user);

            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            verifyQuestionBox.setSelectedIndex(0);
            answerField.setText("");

            viewmodel.setCurrentState(ViewModel.LOGIN_VIEW);
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            verifyQuestionBox.setSelectedIndex(0);
            answerField.setText("");
        }
    }

    private User handleRegister(String username, String email, String password, String securityQuestion, String questionAnswer) {
        try {
            User newUser =  userService.registerUser(username, email, password, securityQuestion, questionAnswer);
            System.out.println("Registration successful! Your User ID: " + newUser.getUserId());
            return newUser;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
