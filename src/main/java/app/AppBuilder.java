package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;

import database.MongoDBOfferDataAccessObject;
import database.MongoDBRequestDataAccessObject;
import entity.SendbirdMessagingService;
import interface_adapter.messaging.MessagingController;
import interface_adapter.messaging.MessagingPresenter;
import interface_adapter.messaging.MessagingViewModel;
import use_case.messaging.MessagingInteractor;
import use_case.messaging.MessagingOutputBoundary;
import view.messaging.MessagingView;
import view.offer_interface.MyOffersView;
import database.MongoDBUserDataAccessObject;
import entity.IDVerfication;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.offers.create_offer.CreateOfferController;
import interface_adapter.offers.create_offer.CreateOfferPresenter;
import interface_adapter.offers.create_offer.CreateOfferViewModel;
import interface_adapter.offers.my_offers.MyOffersController;
import interface_adapter.offers.my_offers.MyOffersViewModel;
import interface_adapter.offers.my_offers.MyOffersPresenter;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfilePresenter;
import interface_adapter.profile.ProfileViewModel;
import use_case.offers.create_offer.CreateOfferInteractor;
import use_case.offers.create_offer.CreateOfferOutputBoundary;
import use_case.offers.create_offer.OfferDataAccessInterface;
import use_case.offers.get_offers.MyOffersInteractor;
import view.ViewManager;
import view.profile_interface.ProfileView;
import view.start_interface.LoginView;
import view.start_interface.SignUpView;
import view.start_interface.VerificationView;
import use_case.profile.ProfileInputBoundary;
import use_case.profile.ProfileInteractor;
import use_case.profile.ProfileOutputBoundary;
import use_case.request.RequestDataAccessInterface;
import use_case.start.UserDataAccessInterface;
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
import javax.swing.JPanel;

public class AppBuilder {
    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private HomepageView homepageView;
    private MyOffersView myOffersView; 
    private VerificationView verificationView;
    private VerificationViewModel verificationViewModel;
    private MessagingViewModel messagingViewModel;
    private MessagingView messagingView;
    private ProfileView profileView;
    private ProfileViewModel profileViewModel;
    private IDVerfication idVerfication;
    private SignupViewModel signupViewModel;
    private SignUpView signUpView;
    private CreateOfferController createOfferController;
    private MyOffersViewModel myOffersViewModel;
    private MyOffersController myOffersController;
    private interface_adapter.offers.my_offers.MyOffersController myOffersViewController;
    private final OfferDataAccessInterface offerDataAccessObject = new MongoDBOfferDataAccessObject();
    private final UserDataAccessInterface userDataAccessObject = new MongoDBUserDataAccessObject();
    private final RequestDataAccessInterface requestDataAccessObject = new MongoDBRequestDataAccessObject();
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
        homepageView = new HomepageView(createOfferController, myOffersController, myOffersViewModel);
        cardPanel.add(homepageView, homepageView.getViewName()); 
        return this;
    }

    public AppBuilder addProfileView() {
        profileViewModel = new ProfileViewModel();
        profileView = new ProfileView(profileViewModel);
        cardPanel.add(profileView, profileView.getViewName());
        return this;
    }

    public AppBuilder addMessagingView() {
        messagingViewModel = new MessagingViewModel();
        messagingView = new MessagingView(messagingViewModel);
        cardPanel.add(messagingView, messagingViewModel.getViewName());
        return this;
    }


    public AppBuilder addMyOffersView() {
        myOffersViewModel = new MyOffersViewModel();
        myOffersView = new MyOffersView(myOffersViewModel);
        cardPanel.add(myOffersView, myOffersView.getViewName());
        return this;
    }

    public AppBuilder addVerificationUseCase() {
        VerificationOutputBoundary verificationPresenter = new VerificationPresenter(homepageView, viewManagerModel,
                verificationViewModel);
        VerificationInputBoundary verificationInteractor = new VerificationInteractor(idVerfication,
                userDataAccessObject, verificationPresenter);
        VerificationController verificationController = new VerificationController(verificationInteractor);
        verificationView.setController(verificationController);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        LoginOutputBoundary loginPresenter = new LoginPresenter(loginViewModel, homepageView, signUpView,
                viewManagerModel);
        LoginInputBoundary loginInteractor = new LoginInteractor(loginPresenter, userDataAccessObject);
        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        SignupOutputBoundary signupPresenter = new SignupPresenter(signupViewModel, viewManagerModel, verificationView,
                loginView, verificationViewModel);
        SignupInputBoundary signupInteractor = new SignupInteractor(signupPresenter, userDataAccessObject);
        SignupController signupController = new SignupController(signupInteractor);
        signUpView.setSignupController(signupController);
        return this;
    }

    public AppBuilder addProfileViewUseCase() {
        ProfileOutputBoundary profileOutputBoundary = new ProfilePresenter(profileViewModel);
        ProfileInputBoundary profileInteractor = new ProfileInteractor(profileOutputBoundary, userDataAccessObject);
        ProfileController profileController = new ProfileController(profileInteractor);
        profileView.setProfileController(profileController);
        return this;
    }

    public AppBuilder addMessagingUseCase() {
        MessagingOutputBoundary messagingPresenter = new MessagingPresenter(messagingViewModel);
        SendbirdMessagingService messagingService = new SendbirdMessagingService();

        MessagingInteractor messagingInteractor = new MessagingInteractor(
                messagingPresenter,
                messagingService,
                requestDataAccessObject,
                offerDataAccessObject);
        MessagingController messagingController = new MessagingController(messagingInteractor);
        messagingView.setMessagingController(messagingController);
        return this;
    }

    public AppBuilder addOfferUseCase() {
        CreateOfferViewModel createOfferViewModel = new CreateOfferViewModel();
        CreateOfferOutputBoundary createOfferPresenter = new CreateOfferPresenter(createOfferViewModel);
        CreateOfferInteractor createOfferInteractor = new CreateOfferInteractor(offerDataAccessObject, createOfferPresenter);
        this.createOfferController = new CreateOfferController(createOfferInteractor);
        return this;
    }

    public AppBuilder addMyOffersUseCase() {
        this.myOffersViewModel = new MyOffersViewModel();
        MyOffersPresenter presenter = new MyOffersPresenter(myOffersViewModel);
        MyOffersInteractor interactor = new MyOffersInteractor(offerDataAccessObject, presenter);
        this.myOffersController = new MyOffersController(interactor);
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
