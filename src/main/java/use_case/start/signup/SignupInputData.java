package use_case.start.signup;

public class SignupInputData {
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String password;
	private final String confirmPassword;
    private boolean verified; 

	public SignupInputData(String firstName, String lastName, String email,
						   String password, String confirmPassword) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
        this.verified = false;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

    public void setVerfified(boolean verified) {
        this.verified = verified;
    }

    public Boolean getVerfied() {
        return this.verified; 
    }
}
