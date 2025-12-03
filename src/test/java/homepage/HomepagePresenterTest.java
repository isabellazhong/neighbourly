package homepage;

import interface_adapter.homepage.ViewModelManager;
import interface_adapter.homepage.HomepagePresenter;
import interface_adapter.homepage.HomepageViewModel;
import view.map.RequestLocation;
import entity.Request;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomepagePresenterTest {

    @Test
    void presentHome_setsViewModelWithRequestsAndCreated() {
        ViewModelManager manager = mock(ViewModelManager.class);
        HomepagePresenter presenter = new HomepagePresenter(manager);

        List<RequestLocation> demo = List.of(
                new RequestLocation("ID-1", "Demo One", 1.0, 2.0, 1.0, 2.0)
        );
        List<Request> created = List.of(new Request("TitleA", "DetailsA", true, null));

        presenter.presentHome(demo, created);

        ArgumentCaptor<HomepageViewModel> captor = ArgumentCaptor.forClass(HomepageViewModel.class);
        verify(manager, times(1)).setViewModel(captor.capture());

        HomepageViewModel vm = captor.getValue();
        assertNotNull(vm, "HomepageViewModel should be provided to ViewModelManager");

        assertEquals(1, vm.getRequests().size(), "demo requests should be propagated");
        assertEquals(demo.get(0).title(), vm.getRequests().get(0).title());

        assertEquals(1, vm.getCreatedRequests().size(), "created requests should be propagated");
        assertEquals("TitleA", vm.getCreatedRequests().get(0).getTitle());
    }

    @Test
    void presentHome_singleArg_setsRequests_and_createdIsNull() {
        ViewModelManager manager = mock(ViewModelManager.class);
        HomepagePresenter presenter = new HomepagePresenter(manager);

        List<RequestLocation> demo = List.of(
                new RequestLocation("ID-2", "Demo Two", 10.0, 20.0, 10.0, 20.0)
        );

        presenter.presentHome(demo);

        ArgumentCaptor<HomepageViewModel> captor = ArgumentCaptor.forClass(HomepageViewModel.class);
        verify(manager, times(1)).setViewModel(captor.capture());

        HomepageViewModel vm = captor.getValue();
        assertNotNull(vm, "HomepageViewModel should be provided to ViewModelManager");
        assertEquals(1, vm.getRequests().size(), "requests should be propagated from single-arg call");
        assertEquals(demo.get(0).title(), vm.getRequests().get(0).title());

        assertNull(vm.getCreatedRequests(), "createdRequests should be null when presenter.presentHome(requests) is used");
    }
}
