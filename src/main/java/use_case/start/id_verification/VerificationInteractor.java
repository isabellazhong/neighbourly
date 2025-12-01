package use_case.start.id_verification;

import entity.Address;
import entity.IDVerfication;
import entity.User;
import use_case.start.UserDataAccessInterface;
import use_case.start.signup.SignupInputData;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.io.File;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class VerificationInteractor implements VerificationInputBoundary {
    private final IDVerfication idVerfication;
    private final UserDataAccessInterface userDataAccessObject;
    private final VerificationOutputBoundary verificationPresenter;
    static final Logger logger = Logger.getLogger(VerificationInteractor.class.getName());

    public VerificationInteractor(IDVerfication idVerfication,
            UserDataAccessInterface userDataAccessObject,
            VerificationOutputBoundary verificationPresenter) {
        this.idVerfication = idVerfication;
        this.userDataAccessObject = userDataAccessObject;
        this.verificationPresenter = verificationPresenter;
    }

    @Override
    public void handleError(String error) {
        verificationPresenter.prepareVerificationErrorView(error);
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
                        UUID.randomUUID(),
                        new Address(address.get("street"),
                                address.get("city"),
                                address.get("region"),
                                address.get("postal_code"),
                                address.get("country")),
                        signupInputData.getPassword());
                userDataAccessObject.addUser(user);
                verificationPresenter.prepareVerificationSuccessButton();
            } else {
                verificationPresenter.prepareVerificationErrorView("Government ID invalid. Please try again.");
            }
            
        } catch (IOException e) {
            logger.severe("Gemini agent could not be called." + e);
        }
    }

    @Override
    public void continueToHomepage() {
        verificationPresenter.prepareVerificationSuccess();
    }

    @Override 
    public void uploadFileStatus(File file) {
        verificationPresenter.prepareUploadFileView(file);
    }

    @Override 
    public void prepareVerifyingView() {
        verificationPresenter.prepareVerifyingView();
    }
}
