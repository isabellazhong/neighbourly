package use_case.start.id_verification;

import java.io.File; 

public interface VerificationInputBoundary {
    void continueToHomepage();
    void uploadFileStatus(File file); 
    void execute(VerificationInputData inputData);
    void handleError(String error); 
    void prepareVerifyingView(); 
}
