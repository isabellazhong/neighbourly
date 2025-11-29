package start_interface;

import entity.Address;
import entity.Gender;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.start.signup.SignupInputData;
import use_case.start.signup.SignupInteractor;
import use_case.start.signup.SignupOutputBoundary;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SignupTest {

	@Test
	void execute_validInputRoutesToVerificationPage() {
		SignupPresenterSpy presenter = new SignupPresenterSpy();
		SignUpInteractorBuilder builder = new SignUpInteractorBuilder(presenter);
		SignupInputData input = buildInputData("Ava", "Li", "ava@example.com", "Secret123!", "Secret123!");

		builder.create().execute(input);

		assertSame(input, presenter.forwardedInputData, "Expected signup flow to continue to verification page");
		assertNull(presenter.emailErrorMessage);
	}

	@Test
	void execute_existingEmailFailsFast() {
		SignupPresenterSpy presenter = new SignupPresenterSpy();
		SignUpInteractorBuilder builder = new SignUpInteractorBuilder(presenter);
		builder.repository.addUser(TestUsers.valid("ava@example.com", "Secret123!"));

		builder.create().execute(buildInputData("Ava", "Li", "ava@example.com", "Secret123!", "Secret123!"));

		assertEquals("User already exists.", presenter.emailErrorMessage);
		assertNull(presenter.forwardedInputData);
	}

	@Test
	void execute_mismatchedPasswordsRaisesConfirmPasswordError() {
		SignupPresenterSpy presenter = new SignupPresenterSpy();
		SignUpInteractorBuilder builder = new SignUpInteractorBuilder(presenter);

		builder.create().execute(buildInputData("Ava", "Li", "ava@example.com", "Secret123!", "Another"));

		assertEquals("Passwords do not match.", presenter.confirmPasswordErrorMessage);
		assertNull(presenter.forwardedInputData);
	}

	private static SignupInputData buildInputData(String firstName,
												  String lastName,
												  String email,
												  String password,
												  String confirmPassword) {
		return new SignupInputData(firstName, lastName, email, password, confirmPassword, Gender.FEMALE);
	}

	private static final class SignupPresenterSpy implements SignupOutputBoundary {
		private String firstNameErrorMessage;
		private String lastNameErrorMessage;
		private String emailErrorMessage;
		private String passwordErrorMessage;
		private String confirmPasswordErrorMessage;
		private String generalErrorMessage;
		private SignupInputData forwardedInputData;
		private boolean backToLogin;

		@Override
		public void prepareFirstNameError(String errorMessage) {
			this.firstNameErrorMessage = errorMessage;
		}

		@Override
		public void prepareLastNameError(String errorMessage) {
			this.lastNameErrorMessage = errorMessage;
		}

		@Override
		public void prepareEmailError(String errorMessage) {
			this.emailErrorMessage = errorMessage;
		}

		@Override
		public void preparePasswordError(String errorMessage) {
			this.passwordErrorMessage = errorMessage;
		}

		@Override
		public void prepareConfirmPasswordError(String errorMessage) {
			this.confirmPasswordErrorMessage = errorMessage;
		}

		@Override
		public void prepareGeneralError(String errorMessage) {
			this.generalErrorMessage = errorMessage;
		}

		@Override
		public void prepareVerificationPage(SignupInputData signupInputData) {
			this.forwardedInputData = signupInputData;
		}

		@Override
		public void prepareBackToLoginPage() {
			this.backToLogin = true;
		}
	}

	private static final class SignUpInteractorBuilder {
		private final SignupPresenterSpy presenter;
		private final InMemoryUserDataAccessObject repository = new InMemoryUserDataAccessObject();

		private SignUpInteractorBuilder(SignupPresenterSpy presenter) {
			this.presenter = presenter;
		}

		private SignupInteractor create() {
			return new SignupInteractor(presenter, repository);
		}
	}

	private static final class TestUsers {
		static User valid(String email, String password) {
			return createUser(email, password);
		}
	}

	private static User createUser(String email, String password) {
		return new User(
				"Test",
				"User",
				email,
				Gender.FEMALE,
				new ArrayList<>(),
				new ArrayList<>(),
				UUID.randomUUID(),
				new Address("1 Castle Rd", "Toronto", "ON", "M5V", "Canada"),
				password);
	}
}
