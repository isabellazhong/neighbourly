package use_case.start;

import entity.User;

public interface UserDataAccessInterface {
    void addUser(User user);

    User getUser(String email, String password) throws Exception;
}
