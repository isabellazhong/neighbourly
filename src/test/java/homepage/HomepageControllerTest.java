package homepage;

import org.junit.jupiter.api.Test;
import use_case.homepage.HomepageInputBoundary;
import interface_adapter.homepage.HomepageController;

import static org.mockito.Mockito.*;

class HomepageControllerTest {

    @Test
    void controller_delegatesToInteractor() {
        HomepageInputBoundary interactor = mock(HomepageInputBoundary.class);
        HomepageController controller = new HomepageController(interactor);

        controller.loadHome();
        verify(interactor, times(1)).loadHome();


        controller.createRequest("T", "D", true);
        verify(interactor, times(1)).createRequest("T", "D", true);

        controller.editRequest("id-1", "New", "ND", false);
        verify(interactor, times(1)).editRequest("id-1", "New", "ND", false);

        controller.deleteRequest("id-1");
        verify(interactor, times(1)).deleteRequest("id-1");
    }
}
