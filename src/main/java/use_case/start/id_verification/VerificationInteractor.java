package use_case.start.id_verification;

import entity.IDVerfication;

public class VerificationInteractor implements VerificationInputBoundary {
    @SuppressWarnings("unused")
    private final IDVerfication idVerfication;

    public VerificationInteractor(IDVerfication idVerfication) {
        this.idVerfication = idVerfication;
    }

    @Override
    public void verifyDocument(VerificationInputData inputData) {
        // TODO: Implement verification workflow (call entity + presenter)
        throw new UnsupportedOperationException("Verification use case not implemented yet.");
    }

    @Override
    public void continueSignup() {
        // TODO: Implement navigation after verification
        throw new UnsupportedOperationException("Continue flow not implemented yet.");
    }
}
