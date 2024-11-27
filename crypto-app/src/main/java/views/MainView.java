package views;

import interface_adapterss.ViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainView extends JFrame implements PropertyChangeListener {

    private CardLayout cardLayout;

    private JPanel views;


    public MainView(LoginView loginView,SignUpView signUpView, ForgetPasswordView forgetPasswordView, ResetView resetView) {
        setTitle("NovaStar");
        cardLayout = new CardLayout();
        views = new JPanel(cardLayout);
        views.add(loginView, ViewModel.LOGIN_VIEW);
        views.add(signUpView, ViewModel.SIGNUP_VIEW);
        views.add(forgetPasswordView,  ViewModel.FORGET_PASSWORD_VIEW);
        views.add(resetView, ViewModel.RESET_VIEW);
        cardLayout.show(views, ViewModel.LOGIN_VIEW);
        this.add(views);
        setBackground(Color.blue);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ViewModel.CURRENT_STATE)) {
            cardLayout.show(views, (String) evt.getNewValue());
        }
    }
}
