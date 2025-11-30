package interface_adapter.interactor;

public class HomepageInputData {
    public enum Action { CREATE_OFFER, CREATE_REQUEST }

    private final Action action;

    public HomepageInputData(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
