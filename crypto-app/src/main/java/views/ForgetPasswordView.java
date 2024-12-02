package views;

import interface_adapterss.UserService;
import interface_adapterss.ViewModel;

import javax.swing.*;

public class ForgetPasswordView extends JPanel{

    private UserService userService = new UserService(
            new interface_adapterss.FirebaseServiceAdapter(), // Pass the FirebaseServiceAdapter
            new interface_adapterss.BlockchainServiceAdapter());

    private final ViewModel viewmodel;

    private JButton verifyButton = new JButton("verify");


    private JTextField resetText = new JTextField();

    public ForgetPasswordView(ViewModel viewmodel) {
        this.viewmodel = viewmodel;

        setLayout(new BoxLayout(ForgetPasswordView.this, BoxLayout.Y_AXIS));

        JLabel reset = new JLabel("enter your email when you create");
        add(reset);

        add(resetText);

        add(verifyButton);

        verifyButton.addActionListener(e -> {
            try{
/*                api.FireBaseAPIClient.checkUserByUserEmail(resetText.getText());
                api.FireBaseAPIClient.checkSecurityQuestionMatchingUserInformation(resetText, verifyQuestion.getText());
                api.FireBaseAPIClient.checkAnswerMatchingSecurityQuestion(verifyQuestion.getText(),answerLabel.getText());*/
                handlePasswordReset(resetText.getText(), new JLabel(""));
                this.viewmodel.setUsername(resetText.getText());
                this.viewmodel.setCurrentState(ViewModel.LOGIN_VIEW);
                resetText.setText("");
            }catch (Exception ex) {
                System.out.println("Error:verify failed, please try again");
                resetText.setText("");
            }
        });
    }

    private void handlePasswordReset(String email, JLabel statusLabel) {
        try {
            userService.resetPassword(email);
            statusLabel.setText("Password reset email sent successfully!");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}
