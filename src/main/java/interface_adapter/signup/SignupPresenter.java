package interface_adapter.signup;

import use_case.start.id_verification.VerificationViewState;
import use_case.start.signup.SignupInputData;
import use_case.start.signup.SignupOutputBoundary;
import use_case.start.signup.SignupState;
import interface_adapter.ViewManagerModel;
import interface_adapter.verification.VerificationViewModel;
import view.start_interface.LoginView;
import view.start_interface.VerificationView;

public class SignupPresenter implements SignupOutputBoundary {
    private final LoginView loginView;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final VerificationView verificationView;
    private final VerificationViewModel verificationViewModel;

    public SignupPresenter(
            SignupViewModel signupViewModel,
            ViewManagerModel viewManagerModel,
            VerificationView verificationView,
            LoginView loginView,
            VerificationViewModel verificationViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.verificationView = verificationView;
        this.signupViewModel = signupViewModel;
        this.verificationViewModel = verificationViewModel;
        this.loginView = loginView;
    }

    public void refreshFromState(SignupState state) {
        state.setFirstNameError(null);
        state.setLastNameError(null);
        state.setPasswordError(null);
        state.setEmailError(null);
        state.setConfirmPasswordError(null);
        state.setGeneralError(null);
    }

    @Override
    public void prepareFirstNameError(String errorMessage) {
        SignupState state = signupViewModel.getState();
        refreshFromState(state);
        state.setFirstNameError(errorMessage);
        signupViewModel.setState(state);
        signupViewModel.firePropertyChange();
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareLastNameError(String errorMessage) {
        SignupState state =  signupViewModel.getState();
        refreshFromState(state);
        state.setLastNameError(errorMessage);
        signupViewModel.setState(state);
        signupViewModel.firePropertyChange();
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareEmailError(String errorMessage) {
        SignupState state =  signupViewModel.getState();
        refreshFromState(state);
        state.setEmailError(errorMessage);
        signupViewModel.setState(state);
        signupViewModel.firePropertyChange();
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void preparePasswordError(String errorMessage) {
        SignupState state =  signupViewModel.getState();
        refreshFromState(state);
        state.setPasswordError(errorMessage);
        signupViewModel.setState(state);
        signupViewModel.firePropertyChange();
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareConfirmPasswordError(String errorMessage) {
        SignupState state = signupViewModel.getState();
        refreshFromState(state);
        state.setConfirmPasswordError(errorMessage);
        signupViewModel.setState(state);
        signupViewModel.firePropertyChange();
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareGeneralError(String errorMessage) {
        SignupState state = signupViewModel.getState();
        refreshFromState(state);
        state.setGeneralError(errorMessage);
        signupViewModel.setState(state);
        signupViewModel.firePropertyChange();
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareVerificationPage(SignupInputData signupInputData) {
        VerificationViewState verificationViewState = new VerificationViewState();
        verificationViewState.setSignupInputData(signupInputData);
        verificationViewModel.setState(verificationViewState);
        viewManagerModel.setState(verificationView.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareBackToLoginPage() {
        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
