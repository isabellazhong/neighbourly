package interface_adapter.signup;

import interface_adapter.ViewModel;
import use_case.start.signup.SignupState;

public class SignupViewModel extends ViewModel<SignupState> {
    public SignupViewModel() {
        super("sign up");
        setState(new SignupState());
    }
}
