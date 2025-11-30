package start_interface;

import entity.Address;
import entity.Gender;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.start.UserDataAccessInterface;
import use_case.start.login.LoginInputData;
import use_case.start.login.LoginInteractor;
import use_case.start.login.LoginOutputBoundary;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

	@Test
	void execute_successfulLoginNotifiesPresenter() {
		InMemoryUserDataAccessObject repository = new InMemoryUserDataAccessObject();
		User existingUser = createUser("paul@example.com", "password123");
		repository.addUser(existingUser);

		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, repository);

		interactor.execute(new LoginInputData("paul@example.com", "password123"));

		assertNotNull(presenter.successUser, "Expected successful login to surface a user");
		assertEquals(existingUser.getEmail(), presenter.successUser.getEmail());
		assertNull(presenter.loginError);
		assertNull(presenter.passwordError);
	}

	@Test
	void execute_wrongPasswordRoutesToPasswordError() {
		InMemoryUserDataAccessObject repository = new InMemoryUserDataAccessObject();
		repository.addUser(createUser("paul@example.com", "password123"));

		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, repository);

		interactor.execute(new LoginInputData("paul@example.com", "wrong"));

		assertEquals("Incorrect password. Please try again", presenter.passwordError);
		assertNull(presenter.successUser);
		assertNull(presenter.loginError);
	}

	@Test
	void execute_unknownUserRoutesToMissingUserError() {
		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, new InMemoryUserDataAccessObject());

		interactor.execute(new LoginInputData("missing@example.com", "password123"));

		assertEquals("User does not exist.", presenter.loginError);
		assertNull(presenter.successUser);
		assertNull(presenter.passwordError);
	}

	@Test
	void execute_blankEmailAndPasswordShowsCombinedError() {
		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, new InMemoryUserDataAccessObject());

		interactor.execute(new LoginInputData("", ""));

		assertEquals("Email and password not entered", presenter.loginError);
		assertNull(presenter.passwordError);
	}

	@Test
	void execute_emptyEmailShowsEmailPrompt() {
		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, new InMemoryUserDataAccessObject());

		interactor.execute(new LoginInputData("   ", "secret"));

		assertEquals("Please enter an email", presenter.loginError);
	}

	@Test
	void execute_invalidEmailShowsFormatError() {
		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, new InMemoryUserDataAccessObject());

		interactor.execute(new LoginInputData("not-an-email", "secret"));

		assertEquals("Invalid email. Please try again", presenter.loginError);
	}

	@Test
	void execute_emptyPasswordShowsPrompt() {
		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, new InMemoryUserDataAccessObject());

		interactor.execute(new LoginInputData("paul@example.com", "  "));

		assertEquals("Please enter your password", presenter.passwordError);
	}

	@Test
	void execute_fetchFailureSurfacesGenericError() {
		FlakyUserDataAccessObject repository = new FlakyUserDataAccessObject();
		User user = createUser("paul@example.com", "password123");
		repository.addUser(user);

		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, repository);

		interactor.execute(new LoginInputData(user.getEmail(), user.getPassword()));

		assertEquals("Unable to fetch. 504 error.", presenter.loginError);
		assertNull(presenter.successUser);
	}

	@Test
	void switchToSignUp_requestsSignupView() {
		LoginPresenterSpy presenter = new LoginPresenterSpy();
		LoginInteractor interactor = new LoginInteractor(presenter, new InMemoryUserDataAccessObject());

		interactor.switchToSignUp();

		assertTrue(presenter.signupViewRequested);
	}

	@Test
	void checkValidUser_unexpectedExceptionThrowsRuntime() {
		LoginInteractor interactor = new LoginInteractor(new LoginPresenterSpy(), new ExplodingUserDataAccessObject());

		assertThrows(RuntimeException.class, () -> interactor.checkValidUser("paul@example.com", "password123"));
	}

	@Test
	void checkValidPassword_unexpectedExceptionThrowsRuntime() {
		LoginInteractor interactor = new LoginInteractor(new LoginPresenterSpy(), new ExplodingUserDataAccessObject());

		assertThrows(RuntimeException.class, () -> interactor.checkValidPassword("paul@example.com", "password123"));
	}

	private static User createUser(String email, String password) {
		return new User(
				"Paul",
				"Example",
				email,
				Gender.MALE,
				new ArrayList<>(),
				new ArrayList<>(),
				UUID.randomUUID(),
				new Address("1 King St", "Toronto", "ON", "M5H", "Canada"),
				password);
	}

	private static final class LoginPresenterSpy implements LoginOutputBoundary {
		private String loginError;
		private String passwordError;
		private User successUser;
		private boolean signupViewRequested;

		@Override
		public void prepareLoginFailInterface(String error) {
			this.loginError = error;
		}

		@Override
		public void prepareWrongPasswordInterface(String error) {
			this.passwordError = error;
		}

		@Override
		public void prepareLoginSucessInterface(User user) {
			this.successUser = user;
		}

		@Override
		public void prepareSignupView() {
			this.signupViewRequested = true;
		}
	}

	private static final class FlakyUserDataAccessObject extends InMemoryUserDataAccessObject {
		private int callCount;

		@Override
		public User getUser(String email, String password) throws Exception {
			callCount++;
			if (callCount >= 3) {
				throw new Exception("Timeout");
			}
			return super.getUser(email, password);
		}
	}

	private static final class ExplodingUserDataAccessObject implements UserDataAccessInterface {
		@Override
		public boolean checkExistingUser(String email) {
			return false;
		}

		@Override
		public void addUser(User user) {
		}

		@Override
		public User getUser(String email, String password) throws Exception {
			throw new Exception("boom");
		}

		@Override
		public boolean updateUser(User user) {
			return false;
		}
	}
}
