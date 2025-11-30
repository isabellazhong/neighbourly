package use_case.start.id_verification;

import java.io.File;

public interface VerificationOutputBoundary {
    void prepareVerificationErrorView(String error);
    void prepareVerficationSuccessButton(); 
    void prepareVerficationSuccess();
    void prepareUploadFileView(File file); 
    void prepareVerifyingView();
}
