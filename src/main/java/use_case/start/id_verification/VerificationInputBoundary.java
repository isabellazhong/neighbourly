package use_case.start.id_verification;

public interface VerificationInputBoundary {
	void verifyDocument(VerificationInputData inputData);

	void continueSignup();
}
