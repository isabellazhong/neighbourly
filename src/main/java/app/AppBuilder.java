package app;

import java.awt.BorderLayout;

import javax.swing.*;

import database.MongoDB;
import entity.Gender;
import entity.User;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfilePresenter;
import interface_adapter.profile.ProfileViewModel;
import view.homepage.HomepageView;
import view.start_interface.LoginView;

import use_case.offer.CreateOfferInteractor;
import use_case.profile.ProfileInteractor;
import interface_adapter.offer.CreateOfferController;

public class AppBuilder {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Neighbourly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        MongoDB mongoDB = new MongoDB();

        User testUser = new User("John", "Doe", "test@example.com", Gender.MALE);
        UserSession.getInstance().setCurrentUser(testUser);

        CreateOfferInteractor offerInteractor = new CreateOfferInteractor(mongoDB);
        CreateOfferController offerController = new CreateOfferController(offerInteractor);

        ProfileViewModel profileViewModel = new ProfileViewModel();
        ProfilePresenter profilePresenter = new ProfilePresenter(profileViewModel);
        ProfileInteractor profileInteractor = new ProfileInteractor(profilePresenter, mongoDB);
        ProfileController profileController = new ProfileController(profileInteractor);

        Runnable onLoginSuccess = () -> SwingUtilities.invokeLater(() -> {
            frame.getContentPane().removeAll();
            HomepageView homepageView = new HomepageView(offerController);
            homepageView.setProfileController(profileController);
            homepageView.setProfileViewModel(profileViewModel);
            frame.add(homepageView, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
        });

        HomepageView homepageView = new HomepageView(offerController);
        homepageView.setProfileController(profileController);
        homepageView.setProfileViewModel(profileViewModel);
        frame.add(homepageView, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
