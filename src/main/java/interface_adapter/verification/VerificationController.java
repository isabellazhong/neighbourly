package interface_adapter.verification;

import use_case.start.id_verification.VerificationInputBoundary;
import use_case.start.id_verification.VerificationInputData;
import use_case.start.signup.SignupInputData;

public class VerificationController {
    private final VerificationInputBoundary verificationInteractor;

    public VerificationController(VerificationInputBoundary verificationInputBoundary) {
        this.verificationInteractor = verificationInputBoundary;
    }

    public void execute(String filePath, SignupInputData signupInputData) {
        VerificationInputData inputData = new VerificationInputData(filePath, signupInputData);
        verificationInteractor.execute(inputData);
    }

    public void continueToHomepage() {
        verificationInteractor.continueToHomepage();
    }

    public void prepareErrorView(String error) {
        verificationInteractor.handleError(error); 
    }
}
