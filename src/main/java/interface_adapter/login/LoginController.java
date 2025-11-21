package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

public class LoginController {
    private LoginInputBoundary loginUseCaseInteractor; 

    public LoginController(LoginInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    public void execute(String email, String password) {
        LoginInputData data = new LoginInputData(email, password); 
        loginUseCaseInteractor.execute(data); 
    }

}
