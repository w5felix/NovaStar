package views;

import entities.User;
import interface_adapterss.ViewModel;

import javax.swing.*;

public class SignUpView extends JPanel {

    private JButton signUpButton = new JButton("Sign Up");

    private JTextField nameField = new JTextField();

    private JTextField emailField = new JTextField();

    private JPasswordField passwordField = new JPasswordField();

    private JComboBox<String> verifyQuestionBox;

    private JPasswordField answerField = new JPasswordField();

    private final ViewModel viewmodel;

    public SignUpView(ViewModel viewmodel) {

        this.viewmodel = viewmodel;

        setLayout(new BoxLayout(SignUpView.this, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Sign Up");
        JLabel nameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel emailLabel = new JLabel("email address:");
        JLabel verifyQuestionLabel = new JLabel("verify question:");
        JLabel answerLabel = new JLabel("answer:");
        String[] questions = {
                "What is your first pet's name",
                "What's your primary school teacher's name",
                "What's your nickname",
                "Where did you born"
        };
        verifyQuestionBox = new JComboBox<>(questions);

        add(titleLabel);
        add(nameLabel);
        add(nameField);
        add(passwordLabel);
        add(passwordField);
        add(emailLabel);
        add(emailField);
        add(verifyQuestionLabel);
        add(verifyQuestionBox);
        add(answerLabel);
        add(answerField);
        add(signUpButton);

        signUpButton.addActionListener(e -> {
            User user = handleRegister(nameField.getName(), emailField.getText(), passwordField.getName(),
                    verifyQuestionBox.getSelectedItem().toString(), answerField.getText());
            viewmodel.setCurrentUser(user);
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            verifyQuestionBox.setSelectedIndex(0);
            answerField.setText("");
            this.viewmodel.setCurrentState(ViewModel.LOGIN_VIEW);
        });
    }

    private User handleRegister(String username,String email, String password, String securityQuestion,
                                String questionAnswer) {
        try {
            User new_user = new User(username, email, password, securityQuestion, questionAnswer);
/*            String userId = api.FireBaseAPIClient.registerUser(username, email,
                    password, securityQuestion, questionAnswer);*/
            System.out.println("Registration successful! Your User ID: " + new_user.getUserId());
            return new_user;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
