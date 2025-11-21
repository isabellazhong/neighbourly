package main.java.use_case.login;

public class LoginInteractor implements LoginInputBoundary {
    LoginOutputBoundary loginPresenter; 
    public LoginInteractor(LoginOutputBoundary loginPresenter) {
        this.loginPresenter = loginPresenter; 
    }

    public boolean checkEmailFormat(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    public boolean checkValidUser(String email) {
        // implement later once mongodb is set up 
        return false;
    }

    public boolean checkValidPassword(String email, String password) {
        // implement later once mongodb is set up 
        return false;
    }

    @Override 
    public void execute(LoginInputData loginInputData) {
        String email = loginInputData.getEmail();
        String password = loginInputData.getPassword(); 

        if (!checkEmailFormat(email)) {
            loginPresenter.prepareLoginFailInterface("Invalid username. Please try again");
        } else if (!checkValidUser(password)) {
            loginPresenter.prepareLoginFailInterface( "User does not exist.");
        } else if (!checkValidPassword(email, password)) {
            loginPresenter.prepareLoginFailInterface("Incorrect password. Please try again");
        } else {
            // TO DO: grab the user entity 
            loginPresenter.prepareLoginSucessInterface();
        }
    }
}
