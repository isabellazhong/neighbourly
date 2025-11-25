package use_case.start.signup;

public class SignupState {
	private String firstName = "";
	private String lastName = "";
	private String email = "";
	private String password = "";
	private String confirmPassword = "";

	private String firstNameError = "";
	private String lastNameError = "";
	private String emailError = "";
	private String passwordError = "";
	private String confirmPasswordError = "";
	private String generalError = "";

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getFirstNameError() {
		return firstNameError;
	}

	public void setFirstNameError(String firstNameError) {
		this.firstNameError = firstNameError;
	}

	public String getLastNameError() {
		return lastNameError;
	}

	public void setLastNameError(String lastNameError) {
		this.lastNameError = lastNameError;
	}

	public String getEmailError() {
		return emailError;
	}

	public void setEmailError(String emailError) {
		this.emailError = emailError;
	}

	public String getPasswordError() {
		return passwordError;
	}

	public void setPasswordError(String passwordError) {
		this.passwordError = passwordError;
	}

	public String getConfirmPasswordError() {
		return confirmPasswordError;
	}

	public void setConfirmPasswordError(String confirmPasswordError) {
		this.confirmPasswordError = confirmPasswordError;
	}

	public String getGeneralError() {
		return generalError;
	}

	public void setGeneralError(String generalError) {
		this.generalError = generalError;
	}
}
