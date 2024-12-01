package views;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;

public class LoginViewTest {

    private LoginView loginView;

    @BeforeEach
    public void setUp() {
        loginView = new LoginView();
    }

    @Test
    public void testInitialization() {
        int expectedComponentCount = 17; // Total components
        int actualComponentCount = loginView.getComponentCount();
        assertEquals(expectedComponentCount, actualComponentCount,
                "LoginView should have " + expectedComponentCount + " components");
    }

    @Test
    public void testGetEmail() {
        JTextField emailField = getComponentByType(loginView, JTextField.class);
        emailField.setText("test@example.com");
        assertEquals("test@example.com", loginView.getEmail(), "Email getter should return the text from the email field");
    }

    @Test
    public void testGetPassword() {
        JPasswordField passwordField = getComponentByType(loginView, JPasswordField.class);
        passwordField.setText("password123");
        assertEquals("password123", loginView.getPassword(), "Password getter should return the text from the password field");
    }

    @Test
    public void testSetStatusMessage() {
        JLabel statusLabel = getComponentByType(loginView, JLabel.class);
        loginView.setStatusMessage("Where Fun Meets Finance!");
        assertEquals("Where Fun Meets Finance!", statusLabel.getText(), "Status label should display the correct message");
    }

    @Test
    public void testLoginButtonActionListener() {
        JButton loginButton = getComponentByType(loginView, JButton.class, "✨ Login ✨");
        final boolean[] wasClicked = {false};

        loginView.addLoginListener(e -> wasClicked[0] = true);

        // Simulate a button click
        for (ActionListener listener : loginButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(loginButton, ActionEvent.ACTION_PERFORMED, null));
        }

        assertTrue(wasClicked[0], "Login button's action listener should be triggered");
    }

    @Test
    public void testRegisterButtonActionListener() {
        JButton registerButton = getComponentByType(loginView, JButton.class, "🎉 Register 🎉");
        final boolean[] wasClicked = {false};

        loginView.addRegisterListener(e -> wasClicked[0] = true);

        // Simulate a button click
        for (ActionListener listener : registerButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(registerButton, ActionEvent.ACTION_PERFORMED, null));
        }

        assertTrue(wasClicked[0], "Register button's action listener should be triggered");
    }

    @Test
    public void testResetPasswordButtonActionListener() {
        JButton resetPasswordButton = getComponentByType(loginView, JButton.class, "💡 Forgot Password?");
        final boolean[] wasClicked = {false};

        loginView.addResetPasswordListener(e -> wasClicked[0] = true);

        // Simulate a button click
        for (ActionListener listener : resetPasswordButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(resetPasswordButton, ActionEvent.ACTION_PERFORMED, null));
        }

        assertTrue(wasClicked[0], "Reset password button's action listener should be triggered");
    }

    // Utility method to find a component of a specific type (and optionally text for buttons/labels)
    private <T extends JComponent> T getComponentByType(JComponent container, Class<T> type) {
        return getComponentByType(container, type, null);
    }

    private <T extends JComponent> T getComponentByType(JComponent container, Class<T> type, String text) {
        for (Component component : container.getComponents()) {
            if (type.isInstance(component)) {
                if (text == null || (component instanceof AbstractButton && text.equals(((AbstractButton) component).getText()))) {
                    return type.cast(component);
                }
            } else if (component instanceof JComponent) {
                T childResult = getComponentByType((JComponent) component, type, text);
                if (childResult != null) {
                    return childResult;
                }
            }
        }
        return null;
    }
}

