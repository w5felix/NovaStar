package views;

import interface_adapterss.ViewModel;

import javax.swing.*;

public class ForgetPasswordView extends JPanel{
    private final ViewModel viewmodel;

    private JButton verifyButton = new JButton("verify");

    private JPasswordField answerField = new JPasswordField();

    private JTextField verifyQuestion = new JTextField();

    private JTextField resetText = new JTextField();

    public ForgetPasswordView(ViewModel viewmodel) {
        this.viewmodel = viewmodel;

        setLayout(new BoxLayout(ForgetPasswordView.this, BoxLayout.Y_AXIS));

        JLabel reset = new JLabel("enter your username or email when you create");
        add(reset);

        add(resetText);

        JLabel verifyQuestionLabel = new JLabel("verify question:");
        add(verifyQuestionLabel);

        add(verifyQuestion);

        JLabel answerLabel = new JLabel("answer:");
        add(answerLabel);

        add(answerField);

        add(verifyButton);

        verifyButton.addActionListener(e -> {
            try{
                api.FireBaseAPIClient.checkUserByUsernameOrEmail(resetText.getText());
                api.FireBaseAPIClient.checkSecurityQuestionMatchingUserInformation(resetText, verifyQuestion.getText());
                api.FireBaseAPIClient.checkAnswerMatchingSecurityQuestion(verifyQuestion.getText(),answerLabel.getText());
                this.viewmodel.setUsername(resetText.getText());
                this.viewmodel.setCurrentState(ViewModel.RESET_VIEW);
                answerField.setText("");
                verifyQuestion.setText("");
                resetText.setText("");
            }catch (Exception ex) {
                System.out.println("Error:verify failed, please try again");
                answerField.setText("");
                verifyQuestion.setText("");
                resetText.setText("");
            }
        });
    }
}
