package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame app = appBuilder
                .addLoginView()
                .addSignupView()
                .addProfileView()
                .addMyOffersView()
                .addOfferUseCase()
                .addMyOffersUseCase()
                .addEditOfferUseCase()
                .addHomePageView()
                .addMessagingView()
                .addVerificationView()
                .addLoginUseCase()
                .addSignupUseCase()
                .addVerificationUseCase()
                .addProfileViewUseCase()
                .addMessagingUseCase()
                .build();
        app.pack();
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }

}
