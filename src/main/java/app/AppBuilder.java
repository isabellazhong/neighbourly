package app;

import java.awt.BorderLayout;
import javax.swing.*;

import interface_adapter.login.LoginViewModel;
import interface_adapter.home.HomepageViewModel;
import interface_adapter.home.ViewModelManager;
import interface_adapter.presenter.HomepagePresenter;
import interface_adapter.interactor.HomepageInteractor;
import interface_adapter.controller.HomepageController;
import interface_adapter.offer.CreateOfferController;

import use_case.offer.CreateOfferInputBoundary;
import use_case.offer.CreateOfferInputData;

import view.homepage.HomepageView;
import view.start_interface.LoginView;
import view.offer_interface.CreateOfferView;

public class AppBuilder {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Neighbourly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

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

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
