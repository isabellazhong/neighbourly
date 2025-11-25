package interface_adapter.login;

import entity.User;
import interface_adapter.ViewManagerModel;
import use_case.start.login.LoginOutputBoundary;
import use_case.start.login.LoginState;
import view.start_interface.SignUpView;
import view.homepage.*;

public class LoginPresenter implements LoginOutputBoundary {
    private LoginViewModel loginViewModel;
    private ViewManagerModel viewManagerModel; 
    private HomepageView homepageView;
    private SignUpView signUpView; 

    public LoginPresenter(LoginViewModel loginViewModel, 
                        HomepageView homepageView, 
                        SignUpView signUpView,
                        ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel; 
        this.loginViewModel = loginViewModel; 
        this.homepageView = homepageView; 
        this.signUpView = signUpView; 
    }

    @Override
    public void prepareLoginFailInterface(String error) {
        LoginState state = new LoginState(); 
        state.setUserError(error);
        loginViewModel.setState(state);
        loginViewModel.firePropertyChange();

        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareWrongPasswordInterface(String error) {
        LoginState state = new LoginState(); 
        state.setPasswordError(error);
        loginViewModel.setState(state);
        loginViewModel.firePropertyChange();

        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareSignupView() {
        viewManagerModel.setState(signUpView.getViewName()); 
        viewManagerModel.firePropertyChange(); 
    }

    @Override
    public void prepareLoginSucessInterface(User user) {
        // add in user logic later 
        viewManagerModel.setState(homepageView.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
