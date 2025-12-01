package interface_adapter.home;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class HomepageViewModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private boolean offerOpen;
    private boolean requestOpen;
    private String errorMessage = " ";

    public boolean isOfferOpen() {
        return offerOpen;
    }

    public void setOfferOpen(boolean offerOpen) {
        boolean old = this.offerOpen;
        this.offerOpen = offerOpen;
        pcs.firePropertyChange("offerOpen", old, offerOpen);
    }

    public boolean isRequestOpen() {
        return requestOpen;
    }

    public void setRequestOpen(boolean requestOpen) {
        boolean old = this.requestOpen;
        this.requestOpen = requestOpen;
        pcs.firePropertyChange("requestOpen", old, requestOpen);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        String old = this.errorMessage;
        this.errorMessage = errorMessage;
        pcs.firePropertyChange("errorMessage", old, errorMessage);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
