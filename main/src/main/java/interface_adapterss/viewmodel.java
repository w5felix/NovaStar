package interface_adapterss;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class viewmodel {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(String name, PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(name, pcl);
    }
}
