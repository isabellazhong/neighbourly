package homepage;

import interface_adapter.homepage.HomepageViewModel;
import view.map.RequestLocation;
import entity.Request;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomepageViewModelTest {

    @Test
    void getters_returnProvidedLists_and_preserveContents() {
        List<RequestLocation> demo = List.of(
                new RequestLocation("REQ-001", "Grocery", 43.65, -79.38, 43.66, -79.39)
        );
        List<Request> created = List.of(
                new Request("MyTitle", "MyDetails", true, null)
        );

        HomepageViewModel vm = new HomepageViewModel(demo, created);

        assertSame(demo, vm.getRequests(), "getRequests should return the same list reference provided");
        assertSame(created, vm.getCreatedRequests(), "getCreatedRequests should return the same list reference provided");

        assertEquals("Grocery", vm.getRequests().get(0).title());
        assertEquals("MyTitle", vm.getCreatedRequests().get(0).getTitle());
    }
}
