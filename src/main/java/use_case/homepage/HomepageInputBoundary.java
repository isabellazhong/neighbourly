package use_case.homepage;

public interface HomepageInputBoundary {
    void loadHome();

    // new CRUD actions for created requests (demo/in-memory implementation expected)
    void createRequest(String title, String details, boolean service);
    void editRequest(String requestId, String newTitle, String newDetails, boolean service);
    void deleteRequest(String requestId);
}
