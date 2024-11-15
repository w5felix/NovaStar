package views;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private CardLayout cardLayout;

    public MainView(LoginView loginView,SignUpView signUpView, ForgetPasswordView forgetPasswordView) {
        setTitle("NovaStar");
        cardLayout = new CardLayout();
        JPanel views = new JPanel(cardLayout);
        views.add(loginView, "login");
        views.add(signUpView, "signUp");
        views.add(forgetPasswordView,  "forgetPassword");
        cardLayout.show(views, "login");
        this.add(views);
        setBackground(Color.blue);
    }
}
