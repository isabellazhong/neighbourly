package app;

import java.awt.BorderLayout;
import javax.swing.*;

import database.MongoDBOfferDataAccessObject;
import database.MongoDBRequestDataAccessObject;
import entity.SendbirdMessagingService;
import interface_adapter.login.LoginViewModel;
import interface_adapter.home.HomepageViewModel;
import interface_adapter.home.ViewModelManager;
import interface_adapter.home.HomepagePresenter;
import interface_adapter.interactor.HomepageInteractor;
import interface_adapter.home.HomepageController;
import interface_adapter.offer.CreateOfferController;

import use_case.offer.CreateOfferInputBoundary;
import use_case.offer.CreateOfferInputData;

import view.homepage.HomepageView;
import view.messaging.MessagingView;
import view.offer_interface.MyOffersView;
import view.start_interface.LoginView;
import view.offer_interface.CreateOfferView;

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }


        Runnable onLoginSuccess = () -> SwingUtilities.invokeLater(() -> {
            frame.getContentPane().removeAll();

            HomepageViewModel homepageViewModel = new HomepageViewModel();
            ViewModelManager viewModelManager = new ViewModelManager(homepageViewModel);
            HomepageViewModel presenterTempModel = new HomepageViewModel();
            HomepagePresenter homepagePresenter = new HomepagePresenter(viewModelManager, presenterTempModel);

            CreateOfferView offerPanel = new CreateOfferView();
            CreateOfferController createOfferController = new
                    CreateOfferController(new CreateOfferInputBoundary() {
                @Override
                public void execute(CreateOfferInputData inputData) {
                    System.out.println("Offer submitted: Title=\"" + inputData.getTitle() + "\" Details=\"" + inputData.getDetails() + "\"");
                }
            });

            offerPanel.setCreateOfferController(createOfferController);
            homepagePresenter.setOfferView(offerPanel);

            HomepageInteractor homepageInteractor = new HomepageInteractor(homepagePresenter);
            HomepageController homepageController = new HomepageController(homepageInteractor);

            HomepageView homepageView = new HomepageView(homepageController, homepageViewModel);
            frame.add(homepageView, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
        });


        LoginView loginView = new LoginView(new LoginViewModel());
        frame.add(loginView, BorderLayout.CENTER);

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
