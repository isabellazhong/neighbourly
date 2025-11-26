package use_case.start.signup;

public interface SignupInputBoundary {
    void execute(SignupInputData signupInputData);
    void switchToVerify(SignupInputData signupInputData);
    void switchToLogin();
}
