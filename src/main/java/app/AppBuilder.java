package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.*;

import database.MongoDBUserDataAccessObject;
import entity.IDVerfication;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfilePresenter;
import interface_adapter.profile.ProfileViewModel;
import view.ViewManager;
import view.homepage.HomepageView;
import view.profile_interface.ProfileView;
import view.start_interface.LoginView;
import view.start_interface.SignUpView;
import view.start_interface.VerificationView;
import use_case.profile.ProfileInputBoundary;
import use_case.profile.ProfileInteractor;
import use_case.profile.ProfileOutputBoundary;
import use_case.start.id_verification.VerificationInputBoundary;
import use_case.start.id_verification.VerificationInteractor;
import use_case.start.id_verification.VerificationOutputBoundary;
import use_case.start.login.LoginInputBoundary;
import use_case.start.login.LoginInteractor;
import use_case.start.login.LoginOutputBoundary;
import use_case.start.signup.SignupInputBoundary;
import use_case.start.signup.SignupInteractor;
import use_case.start.signup.SignupOutputBoundary;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.verification.VerificationPresenter;
import interface_adapter.verification.VerificationController;
import interface_adapter.verification.VerificationViewModel;

public class AppBuilder {
    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private HomepageView homepageView;
    private VerificationView verificationView;
    private VerificationViewModel verificationViewModel;
    private ProfileView profileView; 
    private ProfileViewModel profileViewModel; 
    private IDVerfication idVerfication; 
    private SignupViewModel signupViewModel;
    private SignUpView signUpView;
    private final MongoDBUserDataAccessObject userDataAcessObject = new MongoDBUserDataAccessObject();
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
        idVerfication = new IDVerfication(); 
        verificationViewModel = new VerificationViewModel();
        verificationView = new VerificationView(verificationViewModel);
        cardPanel.add(verificationView, verificationView.getViewName());
        return this;
    }

    public AppBuilder addHomePageView() {
        homepageView = new HomepageView(null); 
        cardPanel.add(homepageView, homepageView.getViewName()); 
        return this;
    }

    public AppBuilder addProfileView() {
        profileViewModel = new ProfileViewModel(); 
        profileView = new ProfileView(profileViewModel); 
        cardPanel.add(profileView, profileView.getViewName());
        return this;
    }

    public AppBuilder addVerificationUseCase() {
        VerificationOutputBoundary verificationPresenter = new VerificationPresenter(homepageView, viewManagerModel, verificationViewModel); 
        VerificationInputBoundary verificationInteractor = new VerificationInteractor(idVerfication, userDataAcessObject, verificationPresenter);
        VerificationController verificationController = new VerificationController(verificationInteractor);
        verificationView.setController(verificationController);
        return this; 
    }

    public AppBuilder addLoginUseCase() {
        LoginOutputBoundary loginPresenter = new LoginPresenter(loginViewModel, homepageView, signUpView,
                viewManagerModel);
        LoginInputBoundary loginInteractor = new LoginInteractor(loginPresenter, userDataAcessObject);
        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        SignupOutputBoundary signupPresenter = new SignupPresenter(signupViewModel, viewManagerModel, verificationView,
                loginView, verificationViewModel);
        SignupInputBoundary signupInteractor = new SignupInteractor(signupPresenter, userDataAcessObject);
        SignupController signupController = new SignupController(signupInteractor);
        signUpView.setSignupController(signupController);
        return this;
    }

    public AppBuilder addProfileViewUseCase() {
        ProfileOutputBoundary profileOutputBoundary = new ProfilePresenter(profileViewModel);
        ProfileInputBoundary profileInteractor = new ProfileInteractor(profileOutputBoundary, userDataAcessObject);
        ProfileController profileController = new ProfileController(profileInteractor);
        profileView.setProfileController(profileController);
        return this;
    }
    public JFrame build() {
        JFrame frame = new JFrame("Neighbourly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(cardPanel, BorderLayout.CENTER);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();
        return frame;
    }
}
