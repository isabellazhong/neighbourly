package use_case.start.id_verification;

import use_case.start.signup.SignupInputBoundary;
import use_case.start.signup.SignupInputData;

public class VerificationViewState {
    private SignupInputData inputData; 
    
    public VerificationViewState() {
    }

    public void setSignupInputData(SignupInputData inputData) {
        this.inputData = inputData; 
    }

    public SignupInputData getSignupInputData() {
        return inputData; 
    }
}
