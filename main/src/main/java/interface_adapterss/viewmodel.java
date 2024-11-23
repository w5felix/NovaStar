package interface_adapterss;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class viewmodel {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        String name = "";
        propertyChangeSupport.addPropertyChangeListener(name, pcl);
    }
}
