package use_case.start.signup;
import java.util.regex.Pattern;
import use_case.start.UserDataAccessInterface;

public class SignupInteractor implements SignupInputBoundary {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private SignupOutputBoundary signUpPresenter;
    private UserDataAccessInterface userDataAccessObject;  

    public SignupInteractor(SignupOutputBoundary signUpPresenter, UserDataAccessInterface userDataAccessObject) {
        this.signUpPresenter = signUpPresenter;
        this.userDataAccessObject = userDataAccessObject; 
    }

    public boolean checkEmptyField(String input) {
        return input == null || input.trim().isEmpty();
    }

    public boolean checkValidName(String name) {
        if (name == null) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public boolean checkCorrectPassword(String password, String verifiedPassword) {
        if (password == null) {
            return false;
        }
        return password.equals(verifiedPassword);
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        String firstName = signupInputData.getFirstName();
        String lastName = signupInputData.getLastName();
        String email = signupInputData.getEmail();
        String password = signupInputData.getPassword();
        String confirmPassword = signupInputData.getConfirmPassword();

        if (checkEmptyField(firstName)) {
            signUpPresenter.prepareFirstNameError("Please enter your first name.");
        } else if (!checkValidName(firstName)) {
            signUpPresenter.prepareFirstNameError("First name cannot contain numbers.");
        } else if(checkEmptyField(lastName)) {
            signUpPresenter.prepareLastNameError("Please enter your last name.");
        } else if (!checkValidName(lastName)) {
            signUpPresenter.prepareLastNameError("Last name cannot contain numbers.");
        } else if (checkEmptyField(email)) {
            signUpPresenter.prepareEmailError("Please enter your email address.");
        } else if (!checkValidEmail(email)) {
            signUpPresenter.prepareEmailError("Please enter a valid email address.");
        } else if (userDataAccessObject.checkExistingUser(email)){
            signUpPresenter.prepareEmailError("User already exists.");
        } else if (checkEmptyField(password)) {
            signUpPresenter.preparePasswordError("Please enter a password.");
        } else if (checkEmptyField(confirmPassword)) {
            signUpPresenter.prepareConfirmPasswordError("Please confirm your password.");
        } else if (!checkCorrectPassword(password, confirmPassword)) {
            signUpPresenter.prepareConfirmPasswordError("Passwords do not match.");
        } else {
            try {
                signUpPresenter.prepareVerificationPage(signupInputData);
            } catch (Exception exception) {
                signUpPresenter.prepareGeneralError("Something went wrong. Please try signing up again.");
            }
        }
    }

    @Override
    public void switchToLogin() {
        signUpPresenter.prepareBackToLoginPage(); 
    }

}
