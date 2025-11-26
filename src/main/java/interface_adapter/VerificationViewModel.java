package interface_adapter;

import interface_adapter.ViewModel;
import use_case.start.signup.SignupState;
import view.start_interface.VerificationView;
import use_case.start.id_verification.*;

public class VerificationViewModel extends ViewModel<VerificationViewState> {
    public VerificationViewModel() {
        super("verify");
        setState(new VerificationViewState());
    }
}
