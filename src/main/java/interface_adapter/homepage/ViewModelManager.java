package interface_adapter.homepage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewModelManager {
    public interface Listener {
        void onViewModelUpdated(HomepageViewModel vm);
    }

    private final List<Listener> listeners = new CopyOnWriteArrayList<>();
    private volatile HomepageViewModel current;

    public void registerListener(Listener l) {
        listeners.add(l);
        if (current != null) {
            l.onViewModelUpdated(current);
        }
    }

    public void unregisterListener(Listener l) {
        listeners.remove(l);
    }

    public void setViewModel(HomepageViewModel vm) {
        this.current = vm;
        for (Listener l : listeners) {
            l.onViewModelUpdated(vm);
        }
    }

    public HomepageViewModel getCurrent() {
        return current;
    }
}
