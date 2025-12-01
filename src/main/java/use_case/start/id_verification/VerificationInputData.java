package use_case.start.id_verification;

import use_case.start.signup.SignupInputData;

public class VerificationInputData {
    private final String filePath;
    private final SignupInputData signupInputData;

    public VerificationInputData(String filePath, SignupInputData signupInputData) {
        this.filePath = filePath;
        this.signupInputData = signupInputData; 
    }

    public String getFilePath() {
        return filePath;
    }

    public SignupInputData getSignupInputData() {
        return signupInputData; 
    }
}
