package interface_adapter.login;

import entity.User;
import use_case.start.login.LoginOutputBoundary;
import use_case.start.login.LoginState;
import view.start_interface.LoginView;
import view.homepage.*;

public class LoginPresenter implements LoginOutputBoundary {
    private LoginViewModel loginViewModel;
    private LoginView loginView; 
    private HomepageView homepageView;

    public LoginPresenter(LoginViewModel loginViewModel, LoginView loginView, HomepageView homepageView) {
        this.loginViewModel = loginViewModel; 
        this.loginView = loginView; 
        this.homepageView = homepageView; 
    }

    @Override
    public void prepareLoginFailInterface(String error) {
        LoginState state = new LoginState(); 
        state.setUserError(error);
        loginViewModel.firePropertyChange();;
    }

    @Override
    public void prepareWrongPasswordInterface(String error) {
        LoginState state = new LoginState(); 
        state.setUserError(error);
        loginViewModel.firePropertyChange();;
    }

    @Override
    public void prepareLoginSucessInterface(User user) {

    }
}
