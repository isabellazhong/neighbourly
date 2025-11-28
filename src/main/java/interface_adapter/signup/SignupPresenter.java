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
    private LoginView loginView;
    private ViewManagerModel viewManagerModel;
    private SignupViewModel signupViewModel;
    private VerificationView verficationView;
    private VerificationViewModel verificationViewModel;

    public SignupPresenter(
            SignupViewModel signupViewModel,
            ViewManagerModel viewManagerModel,
            VerificationView verficationView,
            LoginView loginView,
            VerificationViewModel verificationViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.verficationView = verficationView;
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
        VerificationViewState verficationViewState = new VerificationViewState();
        verficationViewState.setSignupInputData(signupInputData);
        verificationViewModel.setState(verficationViewState);
        viewManagerModel.setState(verficationView.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareBackToLoginPage() {
        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
