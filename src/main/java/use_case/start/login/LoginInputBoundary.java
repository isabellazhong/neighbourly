package use_case.start.login;

public interface LoginInputBoundary {
    void execute(LoginInputData loginInputData); 
    void switchToSignUp(); 
}
