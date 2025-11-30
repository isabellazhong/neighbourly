package start_interface;

import entity.Address;
import entity.Gender;
import entity.User;
import org.junit.jupiter.api.Test;
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
}
