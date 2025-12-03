package interface_adapter.request;

import use_case.request.CreateRequestInteractor;
import use_case.request.EditRequestInteractor;
import use_case.request.DeleteRequestInteractor;

public class RequestController {
    private final CreateRequestInteractor createInteractor;
    private final EditRequestInteractor editInteractor;
    private final DeleteRequestInteractor deleteInteractor;

    public RequestController(CreateRequestInteractor createInteractor,
                             EditRequestInteractor editInteractor,
                             DeleteRequestInteractor deleteInteractor) {
        this.createInteractor = createInteractor;
        this.editInteractor = editInteractor;
        this.deleteInteractor = deleteInteractor;
    }

    public void createRequest(String title, String details, boolean service) {
        createInteractor.execute(title, details, service);
    }

    public void editRequest(String requestId, String newTitle, String newDetails, boolean service) {
        editInteractor.execute(requestId, newTitle, newDetails, service);
    }

    public void deleteRequest(String requestId) {
        deleteInteractor.execute(requestId);
    }
}
