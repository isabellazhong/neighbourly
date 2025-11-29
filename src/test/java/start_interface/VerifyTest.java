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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class VerifyTest {

	@Test
	void execute_successfulVerificationPersistsUser() throws Exception {
		IDVerfication verificationClient = mock(IDVerfication.class);
		MongoDBUserDataAcessObject userDao = mock(MongoDBUserDataAcessObject.class);
		VerificationPresenterSpy presenter = new VerificationPresenterSpy();
		VerificationInteractor interactor = new VerificationInteractor(verificationClient, userDao, presenter);

		SignupInputData signupInputData = new SignupInputData("Test", "User", "ava@example.com", "Secret123!", "Secret123!", Gender.FEMALE);
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
	void continueToHomepage_triggersSuccessNavigation() {
		VerificationPresenterSpy presenter = new VerificationPresenterSpy();
		VerificationInteractor interactor = new VerificationInteractor(mock(IDVerfication.class), mock(MongoDBUserDataAcessObject.class), presenter);

		interactor.continueToHomepage();

		assertTrue(presenter.successFlowCompleted);
	}

	private static String successfulPayload() {
		return "{\"success\": true, \"address\": {"
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
	}
}
