package use_case.request;

import entity.Request;
import java.util.List;
import java.util.UUID;

public interface CreatedRequestRepository {
    void addFirst(Request request);
    boolean update(UUID id, String title, String details, boolean service);
    boolean remove(UUID id);
    List<Request> list();
}
