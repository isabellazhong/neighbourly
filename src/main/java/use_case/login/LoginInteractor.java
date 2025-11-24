package use_case.login;

import database.exceptions.IncorrectPasswordException;
import database.exceptions.UserNotFoundException;

import entity.*;

public class LoginInteractor implements LoginInputBoundary {
    LoginOutputBoundary loginPresenter;
    LoginUserDataAcessInterface userDataAcessObject;

    public LoginInteractor(LoginOutputBoundary loginPresenter, LoginUserDataAcessInterface userDataAccessObject) {
        this.loginPresenter = loginPresenter;
        this.userDataAcessObject = userDataAccessObject;
    }

    public boolean checkEmailFormat(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    public boolean checkValidUser(String email, String password) {
        try {
            try {
                this.userDataAcessObject.getUser(email, password);
            } catch (Exception e) {
                if (e instanceof UserNotFoundException exception) {
                    throw exception;
                }
                throw new RuntimeException(e);
            }
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    public boolean checkValidPassword(String email, String password) {
        try {
            try {
                this.userDataAcessObject.getUser(email, password);
            } catch (Exception e) {
                if (e instanceof IncorrectPasswordException exception) {
                    throw exception;
                }
                throw new RuntimeException(e);
            }
            return true;
        } catch (IncorrectPasswordException e) {
            return false;
        }
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        String email = loginInputData.getEmail();
        String password = loginInputData.getPassword();

        if (!checkEmailFormat(email)) {
            loginPresenter.prepareLoginFailInterface("Invalid username. Please try again");
        } else if (!checkValidUser(email, password)) {
            loginPresenter.prepareLoginFailInterface("User does not exist.");
        } else if (!checkValidPassword(email, password)) {
            loginPresenter.prepareWrongPasswordInterface("Incorrect password. Please try again");;
        } else {
            try {
                User user = this.userDataAcessObject.getUser(email, password);
                loginPresenter.prepareLoginSucessInterface(user);
            } catch (Exception e) {
                loginPresenter.prepareLoginFailInterface("Unable to fetch. 504 error.");
            }
        }
    }
}
