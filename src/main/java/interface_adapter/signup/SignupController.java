package interface_adapter.signup;

import use_case.start.signup.SignupInputBoundary;
import use_case.start.signup.SignupInputData;

public class SignupController {
    private final SignupInputBoundary signupUseCaseInteractor;

    public SignupController(SignupInputBoundary signupUseCaseInteractor) {
        this.signupUseCaseInteractor = signupUseCaseInteractor;
    }

    public void execute(String firstName, String lastName, String email,
                        String password, String confirmPassword) {
        SignupInputData data = new SignupInputData(firstName, lastName, email, password, confirmPassword);
        signupUseCaseInteractor.execute(data);
    }
}
