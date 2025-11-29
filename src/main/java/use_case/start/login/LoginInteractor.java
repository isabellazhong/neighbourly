package use_case.start.login;

import database.exceptions.IncorrectPasswordException;
import database.exceptions.UserNotFoundException;

import entity.*;
import use_case.start.UserDataAccessInterface;

public class LoginInteractor implements LoginInputBoundary {
    LoginOutputBoundary loginPresenter;
    UserDataAccessInterface userDataAcessObject;

    public LoginInteractor(LoginOutputBoundary loginPresenter, UserDataAccessInterface userDataAccessObject) {
        this.loginPresenter = loginPresenter;
        this.userDataAcessObject = userDataAccessObject;
    }

    public boolean checkEmailFormat(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    public boolean checkEmailEmpty(String email) {
        return email == null || email.trim().isEmpty();
    }

    public boolean checkPasswordEmpty(String password) {
        return password == null || password.trim().isEmpty();
    }

    public boolean checkValidUser(String email, String password) {
        try {
            this.userDataAcessObject.getUser(email, password);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        } catch (IncorrectPasswordException e) {
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkValidPassword(String email, String password) {
        try {
            this.userDataAcessObject.getUser(email, password);
            return true;
        } catch (IncorrectPasswordException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        String email = loginInputData.getEmail();
        String password = loginInputData.getPassword();

        if (checkEmailEmpty(email) && checkPasswordEmpty(password)) {
            loginPresenter.prepareLoginFailInterface("Email and password not entered");
        } else if (!checkEmailFormat(email)) {
            if (checkEmailEmpty(email)) {
                loginPresenter.prepareLoginFailInterface("Please enter an email");
            } else {
                loginPresenter.prepareLoginFailInterface("Invalid email. Please try again");
            }
        } else if (checkPasswordEmpty(password)) {
            loginPresenter.prepareWrongPasswordInterface("Please enter your password");
        } else if (!checkValidUser(email, password)) {
            loginPresenter.prepareLoginFailInterface("User does not exist.");
        } else if (!checkValidPassword(email, password)) {
            loginPresenter.prepareWrongPasswordInterface("Incorrect password. Please try again");
        } else {
            try {
                User user = this.userDataAcessObject.getUser(email, password);
                loginPresenter.prepareLoginSucessInterface(user);
            } catch (Exception e) {
                loginPresenter.prepareLoginFailInterface("Unable to fetch. 504 error.");
            }
        }
    }

    public void switchToSignUp() {
        loginPresenter.prepareSignupView();
    }
}
