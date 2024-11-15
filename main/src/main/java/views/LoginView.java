package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.classfile.Label;
import java.util.jar.JarEntry;

public class LoginView extends JPanel implements ActionListener {
    final Font font = new Font("Times New Roman",Font.PLAIN, 25);
    public LoginView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel label1 = new JLabel("Crypto calculator and currency conversion");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setFont(new Font("Times New Roman",Font.BOLD | Font.ITALIC, 30));
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
        JLabel label4 = new JLabel("Username or email address");
        label4.setFont(new Font("Times New Roman",Font.PLAIN, 20));
        label4.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label4);
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(600, 30));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(textField);
        JLabel label5 = new JLabel("Password");
        label5.setFont(new Font("Times New Roman",Font.PLAIN, 20));
        label5.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label5);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(600, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(passwordField);
        add(panel, gbc);

        JPanel panel_1 = new JPanel();
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(this);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel_1.add(loginButton);
        add(panel_1, gbc);

        JPanel panel_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_2.add(new JLabel("New to the APP?"));
        panel_2.add(new JButton("create an account"));
        add(panel_2, gbc);

        JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_3.add(new JLabel("Forget password? Need to reset?"));
        panel_3.add(new JButton("reset"));
        add(panel_3, gbc);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
