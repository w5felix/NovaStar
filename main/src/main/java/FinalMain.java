import org.example.Main;
import views.ForgetPasswordView;
import views.LoginView;
import views.MainView;
import views.SignUpView;

import javax.swing.*;
import java.awt.*;

public class FinalMain {
    public static void main(String[] args) {
        LoginView loginView = new LoginView();
        SignUpView signUpView = new SignUpView();
        ForgetPasswordView forgetPasswordView = new ForgetPasswordView();
        MainView mainView = new MainView(loginView, signUpView, forgetPasswordView);
        mainView.setBackground(Color.blue);
        mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainView.setSize(800, 600);
        //mainView.setIconImage();
        mainView.setVisible(true);
    }
}
