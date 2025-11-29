package interface_adapter.verification;

import interface_adapter.ViewModel;
import use_case.start.id_verification.*;

public class VerificationViewModel extends ViewModel<VerificationViewState> {
    public VerificationViewModel() {
        super("verify");
        setState(new VerificationViewState());
    }
}
