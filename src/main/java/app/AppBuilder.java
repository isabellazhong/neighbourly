// java
package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.*;

import database.MongoDBUserDataAcessObject;
import interface_adapter.VerificationViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import view.ViewManager;
import view.homepage.HomepageView;
import view.start_interface.LoginView;
import view.start_interface.SignUpView;
import view.start_interface.VerificationView;
import use_case.start.login.LoginInputBoundary;
import use_case.start.login.LoginInteractor;
import use_case.start.login.LoginOutputBoundary;
import use_case.start.signup.SignupInputBoundary;
import use_case.start.signup.SignupInteractor;
import use_case.start.signup.SignupOutputBoundary;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;

public class AppBuilder {
    private LoginViewModel loginViewModel; 
    private LoginView loginView; 
    private HomepageView homepageView; 
    private VerificationView verificationView;
    private VerificationViewModel verificationViewModel; 
    private SignupViewModel signupViewModel; 
    private SignUpView signUpView; 
    private final MongoDBUserDataAcessObject userDataAcessObject = new MongoDBUserDataAcessObject();  
    private final ViewManagerModel viewManagerModel = new ViewManagerModel(); 
    private final CardLayout cardLayout = new CardLayout(); 
    private final JPanel cardPanel = new JPanel(); 
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel); 

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel(); 
        signUpView = new SignUpView(signupViewModel);
        cardPanel.add(signUpView, signUpView.getViewName()); 
        return this; 
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel(); 
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName()); 
        return this; 
    }

    public AppBuilder addVerificationView() {
        verificationViewModel = new VerificationViewModel();
        verificationView = new VerificationView(verificationViewModel);
        cardPanel.add(verificationView, verificationView.getViewName());
        return this;
    }

    public AppBuilder addLoginUseCase() {
        LoginOutputBoundary loginPresenter = new LoginPresenter(loginViewModel, homepageView, signUpView, viewManagerModel);
        LoginInputBoundary loginInteractor = new LoginInteractor(loginPresenter, userDataAcessObject);
        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this; 
    }

    public AppBuilder addSignupUseCase() {
        SignupOutputBoundary signupPresenter = new SignupPresenter(signupViewModel, viewManagerModel, verificationView, loginView, verificationViewModel);
        SignupInputBoundary signupInteractor = new SignupInteractor(signupPresenter); 
        SignupController signupController = new SignupController(signupInteractor);
        signUpView.setSignupController(signupController);
        return this; 
    }

    public JFrame build() {
        JFrame frame = new JFrame("Neighbourly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(cardPanel, BorderLayout.CENTER);

        viewManagerModel.setState(verificationView.getViewName());
        viewManagerModel.firePropertyChange();
        return frame; 
    }
}
