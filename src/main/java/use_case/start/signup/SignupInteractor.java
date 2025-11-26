package use_case.start.signup;

import java.util.regex.Pattern;

public class SignupInteractor implements SignupInputBoundary {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private SignupOutputBoundary signUpPresenter;

    public SignupInteractor(SignupOutputBoundary signUpPresenter) {
        this.signUpPresenter = signUpPresenter;
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
        }

        if (!checkValidName(firstName)) {
            signUpPresenter.prepareFirstNameError("First name cannot contain numbers.");
        }

        if (checkEmptyField(lastName)) {
            signUpPresenter.prepareLastNameError("Please enter your last name.");
        }

        if (!checkValidName(lastName)) {
            signUpPresenter.prepareLastNameError("Last name cannot contain numbers.");
        }

        if (checkEmptyField(email)) {
            signUpPresenter.prepareEmailError("Please enter your email address.");
        }

        if (!checkValidEmail(email)) {
            signUpPresenter.prepareEmailError("Please enter a valid email address.");
        }

        if (checkEmptyField(password)) {
            signUpPresenter.preparePasswordError("Please enter a password.");
        }

        if (checkEmptyField(confirmPassword)) {
            signUpPresenter.prepareConfirmPasswordError("Please confirm your password.");
        }

        if (!checkCorrectPassword(password, confirmPassword)) {
            signUpPresenter.prepareConfirmPasswordError("Passwords do not match.");
        }
    }

    @Override
    public void switchToVerify(SignupInputData signupInputData) {
        try {
            signUpPresenter.prepareVerificationPage(signupInputData);
        } catch (Exception exception) {
            signUpPresenter.prepareGeneralError("Something went wrong. Please try signing up again.");
        }
    }

    @Override
    public void switchToLogin() {
        signUpPresenter.prepareBackToLoginPage(); 
    }

}
