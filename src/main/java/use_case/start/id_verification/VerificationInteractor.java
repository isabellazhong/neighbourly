package use_case.start.id_verification;

import database.MongoDBUserDataAcessObject;
import entity.Address;
import entity.Gender;
import entity.IDVerfication;
import entity.User;
import interface_adapter.verification.VerficationPresenter;
import use_case.start.signup.SignupInputData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

public class VerificationInteractor implements VerificationInputBoundary {
    private final IDVerfication idVerfication;
    private final MongoDBUserDataAcessObject userDataAcessObject;
    private final VerificationOutputBoundary verficationPresenter;

    public VerificationInteractor(IDVerfication idVerfication,
            MongoDBUserDataAcessObject userDataAcessObject,
            VerificationOutputBoundary verficationPresenter) {
        this.idVerfication = idVerfication;
        this.userDataAcessObject = userDataAcessObject;
        this.verficationPresenter = verficationPresenter;
    }

    @Override
    public void execute(VerificationInputData inputData) {
        try {
            // allows for conversion of string to json
            Gson gson = new Gson();
            String responseString = idVerfication.getResponse(inputData.getFilePath());
            @SuppressWarnings("unchecked")
            Map<String, Object> responseJSON = gson.fromJson(responseString, Map.class);
            Boolean success = (Boolean) responseJSON.get("success");
            @SuppressWarnings("unchecked")
            Map<String, String> address = (Map<String, String>) responseJSON.get("address");
            SignupInputData signupInputData = inputData.getSignupInputData();

            if (Boolean.TRUE.equals(success)) {
                User user = new User(
                        signupInputData.getFirstName(),
                        signupInputData.getLastName(),
                        signupInputData.getEmail(),
                        signupInputData.getGender(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        UUID.randomUUID(),
                        new Address(address.get("street"),
                                address.get("city"),
                                address.get("region"),
                                address.get("postal_code"),
                                address.get("country")),
                        signupInputData.getPassword());
                userDataAcessObject.addUser(user);
            } else {
                verficationPresenter.prepareVerificationErrorView();
            }
        } catch (IOException e) {
            System.out.println("Gemini agent could not be called." + e);
            return;
        }
    }

    @Override
    public void continueToHomepage() {
        verficationPresenter.prepareVerficationSuccess();
    }
}
