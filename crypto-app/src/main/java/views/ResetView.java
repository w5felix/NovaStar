/*
package views;

import interface_adapterss.ViewModel;

import javax.swing.*;

public class ResetView extends JPanel {

    private final ViewModel viewmodel;

    private JPasswordField passwordField = new JPasswordField();

    private JButton resetButton = new JButton("Reset");

    public ResetView(ViewModel viewmodel) {
        this.viewmodel = viewmodel;

        setLayout(new BoxLayout(ResetView.this, BoxLayout.Y_AXIS));
        JLabel resetPassword = new JLabel("Reset Password");
        add(resetPassword);
        add(passwordField);
        add(resetButton);

        resetButton.addActionListener(e -> {
            try{
                api.FireBaseAPIClient.changePasswordByUsernameOrEmail(this.viewmodel.getUsername());
                System.out.println("Congratulations, your password has been reset");
                this.viewmodel.setCurrentState(ViewModel.LOGIN_VIEW);
                passwordField.setText("");
            }catch (Exception ex){
                System.out.println("Please reset your password again");
                passwordField.setText("");
            }
        });
    }
}
*/
