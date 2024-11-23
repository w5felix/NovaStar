package views;

import javax.swing.*;

public class SignUpView extends JPanel {

    private JButton signUpButton = new JButton("Sign Up");

    private JTextField nameField = new JTextField();

    private JTextField emailField = new JTextField();

    private JPasswordField passwordField = new JPasswordField();

    private JTextField usernameField = new JTextField();

    private JComboBox<String> verifyQuestionBox;

    private JPasswordField answerField = new JPasswordField();

    public SignUpView() {
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
    }
}
