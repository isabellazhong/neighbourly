package use_case.start.id_verification;

public interface VerificationOutputBoundary {
    void prepareVerificationErrorView(String error);
    void prepareVerficationSuccessButton(); 
    void prepareVerficationSuccess();
}
