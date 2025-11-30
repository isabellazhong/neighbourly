package interface_adapter.home;

import javax.swing.SwingUtilities;

public class ViewModelManager {
    private final HomepageViewModel canonical;

    public ViewModelManager(HomepageViewModel canonical) {
        this.canonical = canonical;
    }

    public void refreshFrom(HomepageViewModel incoming) {
        SwingUtilities.invokeLater(() -> {
            canonical.setOfferOpen(incoming.isOfferOpen());
            canonical.setRequestOpen(incoming.isRequestOpen());
            canonical.setErrorMessage(incoming.getErrorMessage());
        });
    }
}
