package use_case.login;

import entity.User;

public interface LoginUserDataAcessInterface {
    void addUser(User user);

    User getUser(String email, String password) throws Exception;
}
