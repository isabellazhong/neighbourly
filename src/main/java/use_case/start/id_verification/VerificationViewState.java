package use_case.start.id_verification;

import use_case.start.signup.SignupInputData;

public class VerificationViewState {
    private SignupInputData signupInputData;
    private String selectedFileName;
    private String selectedFilePath;
    private String statusMessage;
    private boolean verificationSuccessful;
    private boolean verifying;
    private String errorMessage;

    public VerificationViewState() {
        this.selectedFileName = "No file selected";
        this.selectedFilePath = null;
        this.statusMessage = "Upload a government-issued ID to continue.";
        this.verificationSuccessful = false;
        this.verifying = false;
        this.errorMessage = "";
    }

    public void setSignupInputData(SignupInputData inputData) {
        this.signupInputData = inputData;
    }

    public SignupInputData getSignupInputData() {
        return signupInputData;
    }

    public String getSelectedFileName() {
        return selectedFileName;
    }

    public void setSelectedFileName(String selectedFileName) {
        this.selectedFileName = selectedFileName;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isVerificationSuccessful() {
        return verificationSuccessful;
    }

    public void setVerificationSuccessful(boolean verificationSuccessful) {
        this.verificationSuccessful = verificationSuccessful;
    }

    public boolean isVerifying() {
        return verifying;
    }

    public void setVerifying(boolean verifying) {
        this.verifying = verifying;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void resetVerificationFlags() {
        this.verificationSuccessful = false;
        this.verifying = false;
    }

    public String getSelectedFilePath() {
        return selectedFilePath;
    }

    public void setSelectedFilePath(String selectedFilePath) {
        this.selectedFilePath = selectedFilePath;
    }
}
