package use_case.start;

import entity.User;

public interface UserDataAccessInterface {
    boolean checkExistingUser(String email);
    void addUser(User user);
    User getUser(String email, String password) throws Exception;
}
