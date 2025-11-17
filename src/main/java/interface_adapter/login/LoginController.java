package main.java.interface_adapter.login;

import main.java.use_case.login.LoginInputBoundary;
import main.java.use_case.login.LoginInputData;

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
