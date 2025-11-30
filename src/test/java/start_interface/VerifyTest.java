package start_interface;

import database.MongoDBUserDataAcessObject;
import entity.Gender;
import entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.start.id_verification.VerificationInputData;
import use_case.start.id_verification.VerificationInteractor;
import use_case.start.id_verification.VerificationOutputBoundary;
import use_case.start.signup.SignupInputData;
import entity.IDVerfication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.IOException;

class VerifyTest {

    @Test
    void execute_successfulVerificationPersistsUser() throws Exception {
        IDVerfication verificationClient = mock(IDVerfication.class);
        MongoDBUserDataAcessObject userDao = mock(MongoDBUserDataAcessObject.class);
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(verificationClient, userDao, presenter);

        SignupInputData signupInputData = new SignupInputData("Test", "User", "ava@example.com", "Secret123!",
                "Secret123!", Gender.FEMALE);
        VerificationInputData verificationInputData = new VerificationInputData("/fake/path/id.png", signupInputData);

        when(verificationClient.getResponse(anyString())).thenReturn(successfulPayload());

        interactor.execute(verificationInputData);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).addUser(userCaptor.capture());
        assertEquals(signupInputData.getEmail(), userCaptor.getValue().getEmail());
        assertTrue(presenter.successButtonPrepared, "Presenter should show success button once verification passes");
        assertNull(presenter.errorMessage);
    }

    @Test
    void execute_successfulVerificationCopiesAllSignupDetails() throws Exception {
        IDVerfication verificationClient = mock(IDVerfication.class);
        MongoDBUserDataAcessObject userDao = mock(MongoDBUserDataAcessObject.class);
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(verificationClient, userDao, presenter);

        SignupInputData signupInputData = new SignupInputData("Ava", "Li", "ava@example.com", "Secret123!",
                "Secret123!", Gender.FEMALE);
        VerificationInputData verificationInputData = new VerificationInputData("/fake/path/id.png", signupInputData);

        when(verificationClient.getResponse(anyString())).thenReturn(successfulPayload());

        interactor.execute(verificationInputData);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).addUser(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("Ava", savedUser.getName());
        assertEquals("Li", savedUser.getLastName());
        assertEquals("Female", savedUser.getGender());
        assertEquals("Secret123!", savedUser.getPassword());
        assertEquals("1 King St", savedUser.getAddress().getStreet());
        assertEquals("Toronto", savedUser.getAddress().getCity());
        assertEquals("ON", savedUser.getAddress().getRegion());
        assertEquals("M5H", savedUser.getAddress().getPostalCode());
        assertEquals("Canada", savedUser.getAddress().getCountry());
    }

    @Test
    void execute_failedVerificationSurfacesError() throws Exception {
        IDVerfication verificationClient = mock(IDVerfication.class);
        MongoDBUserDataAcessObject userDao = mock(MongoDBUserDataAcessObject.class);
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(verificationClient, userDao, presenter);

        SignupInputData signupInputData = new SignupInputData("Test", "User", "ava@example.com", "Secret123!",
                "Secret123!", Gender.FEMALE);
        VerificationInputData verificationInputData = new VerificationInputData("/fake/path/id.png", signupInputData);

        when(verificationClient.getResponse(anyString())).thenReturn(failedPayload());

        interactor.execute(verificationInputData);

        verify(userDao, never()).addUser(any());
        assertEquals("Government ID inavlid. Please try again.", presenter.errorMessage);
        assertFalse(presenter.successButtonPrepared);
    }

    @Test
    void execute_missingSuccessFlagTreatsVerificationAsFailed() throws Exception {
        IDVerfication verificationClient = mock(IDVerfication.class);
        MongoDBUserDataAcessObject userDao = mock(MongoDBUserDataAcessObject.class);
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(verificationClient, userDao, presenter);

        SignupInputData signupInputData = new SignupInputData("Test", "User", "ava@example.com", "Secret123!",
                "Secret123!", Gender.FEMALE);
        VerificationInputData verificationInputData = new VerificationInputData("/fake/path/id.png", signupInputData);

        when(verificationClient.getResponse(anyString())).thenReturn(payloadWithoutSuccessFlag());

        interactor.execute(verificationInputData);

        verify(userDao, never()).addUser(any());
        assertEquals("Government ID inavlid. Please try again.", presenter.errorMessage);
    }

    @Test
    void execute_handlesVerificationClientIOException() throws Exception {
        IDVerfication verificationClient = mock(IDVerfication.class);
        MongoDBUserDataAcessObject userDao = mock(MongoDBUserDataAcessObject.class);
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(verificationClient, userDao, presenter);

        SignupInputData signupInputData = new SignupInputData("Test", "User", "ava@example.com", "Secret123!",
                "Secret123!", Gender.FEMALE);
        VerificationInputData verificationInputData = new VerificationInputData("/fake/path/id.png", signupInputData);

        when(verificationClient.getResponse(anyString())).thenThrow(new IOException("down"));

        interactor.execute(verificationInputData);

        verify(userDao, never()).addUser(any());
        assertFalse(presenter.successButtonPrepared);
        assertNull(presenter.errorMessage);
    }

    @Test
    void continueToHomepage_triggersSuccessNavigation() {
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(mock(IDVerfication.class),
                mock(MongoDBUserDataAcessObject.class), presenter);

		interactor.continueToHomepage();

		assertTrue(presenter.successFlowCompleted);
	}

    @Test
    void uploadFileStatus_notifiesPresenterOfFileProgress() {
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(mock(IDVerfication.class),
                mock(MongoDBUserDataAcessObject.class), presenter);
        File uploadedFile = new File("/tmp/mock-id.png");

        interactor.uploadFileStatus(uploadedFile);

        assertEquals(uploadedFile, presenter.uploadedFile);
    }

    @Test
    void prepareVerifyingView_notifiesPresenter() {
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(mock(IDVerfication.class),
                mock(MongoDBUserDataAcessObject.class), presenter);

        interactor.prepareVerifyingView();

        assertTrue(presenter.verifyingRequested);
    }

    @Test
    void handleError_delegatesToPresenter() {
        VerificationPresenterSpy presenter = new VerificationPresenterSpy();
        VerificationInteractor interactor = new VerificationInteractor(mock(IDVerfication.class),
                mock(MongoDBUserDataAcessObject.class), presenter);

        interactor.handleError("boom");

        assertEquals("boom", presenter.errorMessage);
    }

    private static String successfulPayload() {
        return "{\"success\": true, \"address\": {"
                + "\"street\": \"1 King St\","
                + "\"city\": \"Toronto\","
                + "\"region\": \"ON\","
                + "\"postal_code\": \"M5H\","
                + "\"country\": \"Canada\"}}";
    }

    private static String failedPayload() {
        return "{\"success\": false}";
    }

    private static String payloadWithoutSuccessFlag() {
        return "{\"address\": {"
                + "\"street\": \"1 King St\","
                + "\"city\": \"Toronto\","
                + "\"region\": \"ON\","
                + "\"postal_code\": \"M5H\","
                + "\"country\": \"Canada\"}}";
    }

    private static final class VerificationPresenterSpy implements VerificationOutputBoundary {
        private String errorMessage;
        private boolean successButtonPrepared;
        private boolean successFlowCompleted;
        private File uploadedFile;
        private boolean verifyingRequested;

        @Override
        public void prepareVerificationErrorView(String error) {
            this.errorMessage = error;
        }

        @Override
        public void prepareVerficationSuccessButton() {
            this.successButtonPrepared = true;
        }

        @Override
        public void prepareVerficationSuccess() {
            this.successFlowCompleted = true;
        }

        @Override
        public void prepareUploadFileView(File file) {
            this.uploadedFile = file;
        }

        @Override
        public void prepareVerifyingView() {
            this.verifyingRequested = true;
        }
    }
}
