package use_case.start.id_verification;

public interface VerificationInputBoundary {
    void continueToHomepage();

    void execute(VerificationInputData inputData);
    void handleError(String error); 
}
