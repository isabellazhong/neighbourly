package homepage;

import interface_adapter.homepage.ViewModelManager;
import interface_adapter.homepage.HomepageViewModel;
import view.map.RequestLocation;
import entity.Request;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewModelManagerTest {

    private static final class SpyListener implements ViewModelManager.Listener {
        HomepageViewModel last;
        int calls = 0;

        @Override
        public void onViewModelUpdated(HomepageViewModel vm) {
            this.last = vm;
            this.calls++;
        }
    }

    @Test
    void registerListener_receivesCurrentImmediately_whenCurrentSet() {
        ViewModelManager manager = new ViewModelManager();
        HomepageViewModel vm = new HomepageViewModel(
                List.of(new RequestLocation("REQ-001", "Demo", 1.0, 2.0, 1.0, 2.0)),
                List.of(new Request("T", "D", true, null))
        );

        manager.setViewModel(vm);

        SpyListener listener = new SpyListener();
        manager.registerListener(listener);

        assertSame(vm, listener.last, "Listener should receive current view model immediately on register");
        assertEquals(1, listener.calls, "Listener should have been called exactly once");
    }

    @Test
    void setViewModel_notifiesRegisteredListeners_and_getCurrentReturnsLast() {
        ViewModelManager manager = new ViewModelManager();
        SpyListener listener = new SpyListener();
        manager.registerListener(listener);

        HomepageViewModel vm = new HomepageViewModel(
                List.of(new RequestLocation("REQ-002", "Other", 3.0, 4.0, 1.0, 2.0)),
                List.of()
        );

        manager.setViewModel(vm);

        assertSame(vm, manager.getCurrent(), "getCurrent should return the last set HomepageViewModel");
        assertSame(vm, listener.last, "Registered listener should be notified with the vm");
        assertEquals(1, listener.calls);
    }

    @Test
    void unregisterListener_stopsNotifications() {
        ViewModelManager manager = new ViewModelManager();
        SpyListener listener = new SpyListener();
        manager.registerListener(listener);
        manager.unregisterListener(listener);

        HomepageViewModel vm = new HomepageViewModel(
                List.of(new RequestLocation("REQ-003", "Later", 5.0, 6.0, 1.0, 2.0)),
                List.of()
        );

        manager.setViewModel(vm);

        assertNull(listener.last, "Unregistered listener should not receive further updates");
        assertEquals(0, listener.calls, "Unregistered listener should not be called");
    }
}
