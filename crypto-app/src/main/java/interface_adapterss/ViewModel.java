package interface_adapterss;

import entities.User;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ViewModel {

    public static final String CURRENT_STATE = "currentState";

    public static final String LOGIN_VIEW = "loginView";

    public static final String SIGNUP_VIEW = "signupView";

    public static final String FORGET_PASSWORD_VIEW = "forgetPasswordView";

    public static final String AFTER_LOGIN_VIEW = "afterLogin";

    public static final String TRANSACTION_VIEW = "transaction";

    private String currentState;

    private User currentUser;

    private String username;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public ViewModel() {
        this.currentState = LOGIN_VIEW;
    }

    public String getCurrentUsername() {
        return currentUser.getName();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrentState(String state) {
        String priviousstate = this.currentState;
        this.currentState = state;
        propertyChangeSupport.firePropertyChange(CURRENT_STATE, priviousstate, state);
    }

    public void addPropertyChangeListener(String name, PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(name, pcl);
    }
}
