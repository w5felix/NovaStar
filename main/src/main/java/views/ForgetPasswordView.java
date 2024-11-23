package views;

import interface_adapterss.ViewModel;

import javax.swing.*;

public class ForgetPasswordView extends JPanel{
    private final ViewModel viewmodel;

    private JButton resetButton = new JButton("Reset");

    private JPasswordField answerField = new JPasswordField();

    private JTextField verifyQuestion = new JTextField();

    public ForgetPasswordView(ViewModel viewmodel) {
        this.viewmodel = viewmodel;

        setLayout(new BoxLayout(ForgetPasswordView.this, BoxLayout.Y_AXIS));

        JLabel reset = new JLabel("enter your password or email when you create");
        add(reset);

        JTextField resetText = new JTextField();
        add(resetText);

        JLabel verifyQuestionLabel = new JLabel("verify question:");
        add(verifyQuestionLabel);

        add(verifyQuestion);

        JLabel answerLabel = new JLabel("answer:");
        add(answerLabel);

        add(answerField);

        add(resetButton);

        resetButton.addActionListener(e -> {
            this.viewmodel.setCurrentState(ViewModel.LOGIN_VIEW);
        });

    }
}
