package interface_adapter.verification;

import use_case.start.id_verification.VerificationInputBoundary;
import use_case.start.id_verification.VerificationInputData;

public class VerificationController {
    private final VerificationInputBoundary verificationInputBoundary;

    public VerificationController(VerificationInputBoundary verificationInputBoundary) {
        this.verificationInputBoundary = verificationInputBoundary;
    }

    public void verifyDocument(String filePath) {
        VerificationInputData inputData = new VerificationInputData(filePath);
        verificationInputBoundary.verifyDocument(inputData);
    }

    public void continueSignup() {
        verificationInputBoundary.continueSignup();
    }
}
