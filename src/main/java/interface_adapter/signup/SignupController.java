package interface_adapter.signup;

import entity.Gender;
import use_case.start.signup.SignupInputBoundary;
import use_case.start.signup.SignupInputData;

public class SignupController {
    private final SignupInputBoundary signupUseCaseInteractor;

    public SignupController(SignupInputBoundary signupUseCaseInteractor) {
        this.signupUseCaseInteractor = signupUseCaseInteractor;
    }

    public void execute(String firstName, String lastName, String email,
                        String password, String confirmPassword) {
        // add in the gender option later 
        SignupInputData data = new SignupInputData(firstName, lastName, email, password, confirmPassword,  Gender.FEMALE);
        signupUseCaseInteractor.execute(data);
    }

    public void switchToLogin() {
        signupUseCaseInteractor.switchToLogin();
    }
}
