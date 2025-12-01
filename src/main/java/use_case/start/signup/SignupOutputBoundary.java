package use_case.start.signup;

public interface SignupOutputBoundary {
    void prepareFirstNameError(String errorMessage);

    void prepareLastNameError(String errorMessage);

    void prepareEmailError(String errorMessage);

    void preparePasswordError(String errorMessage);

    void prepareConfirmPasswordError(String errorMessage);

    void prepareGeneralError(String errorMessage);

    void prepareVerificationPage(SignupInputData signupInputData);

    void prepareBackToLoginPage(); 
}
