package start_interface;

import database.exceptions.IncorrectPasswordException;
import database.exceptions.UserNotFoundException;
import entity.User;
import use_case.start.UserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

class InMemoryUserDataAccessObject implements UserDataAccessInterface {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public boolean checkExistingUser(String email) {
        return users.containsKey(email);
    }

    @Override
    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public User getUser(String email, String password) throws Exception {
        User user = users.get(email);
        if (user == null) {
            throw new UserNotFoundException("User does not exist.");
        }
        if (!user.getPassword().equals(password)) {
            throw new IncorrectPasswordException("Incorrect password.");
        }
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        users.put(user.getEmail(), user);
        return true;
    }
}
