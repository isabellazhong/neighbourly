package app;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder(); 
        JFrame app = appBuilder
                    .addLoginView()
                    .addSignupView()
                    .addProfileView()
                    .addOfferUseCase()
                    .addMyOffersUseCase()
                    .addHomePageView()
                    .addVerificationView()
                    .addLoginUseCase()
                    .addSignupUseCase()
                    .addVerificationUseCase()
                    .addProfileViewUseCase()
                    .build(); 
        app.pack();
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }
    
}
