package use_case.start.id_verification;

public class VerificationInputData {
	private final String filePath;

	public VerificationInputData(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
}
