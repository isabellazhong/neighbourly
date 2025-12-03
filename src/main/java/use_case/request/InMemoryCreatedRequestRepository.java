package use_case.request;

import entity.Request;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryCreatedRequestRepository implements CreatedRequestRepository {
    private final List<Request> store = new ArrayList<>();

    @Override
    public synchronized void addFirst(Request request) {
        store.add(0, request);
    }

    @Override
    public synchronized boolean update(UUID id, String title, String details, boolean service) {
        for (Request r : store) {
            if (r.getId().equals(id)) {
                r.setTitle(title);
                r.setDetails(details);
                r.setService(service);
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean remove(UUID id) {
        return store.removeIf(r -> r.getId().equals(id));
    }

    @Override
    public synchronized List<Request> list() {
        return List.copyOf(store);
    }
}
