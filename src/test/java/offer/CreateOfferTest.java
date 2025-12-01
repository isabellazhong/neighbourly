package offer;

import app.UserSession;
import entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.offers.create_offer.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOfferTest {

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(UUID.randomUUID(), "Test", "User", "test@example.com", "MALE");
        UserSession.getInstance().setCurrentUser(mockUser);
    }

    @AfterEach
    void tearDown() {
        UserSession.getInstance().setCurrentUser(null);
    }

    @Test
    void execute_successfulCreationNotifiesPresenter() {
        InMemoryOfferDataAccessObject repository = new InMemoryOfferDataAccessObject();
        CreateOfferPresenterSpy presenter = new CreateOfferPresenterSpy();
        CreateOfferInteractor interactor = new CreateOfferInteractor(repository, presenter);

        CreateOfferInputData inputData = new CreateOfferInputData("Dog Walking", "Can offer to walk dogs");

        interactor.execute(inputData);

        assertNotNull(presenter.successData);
        assertNull(presenter.failError);
        assertEquals("Dog Walking", presenter.successData.getOffer().getTitle());

        assertEquals(1, repository.allOffers().size());
        assertEquals(mockUser.getID(), repository.allOffers().get(0).getUserID());

        assertFalse(presenter.successData.isUseCaseFailed());
    }

    @Test
    void execute_titleOnlyIsSuccessful() {
        InMemoryOfferDataAccessObject repository = new InMemoryOfferDataAccessObject();
        CreateOfferPresenterSpy presenter = new CreateOfferPresenterSpy();
        CreateOfferInteractor interactor = new CreateOfferInteractor(repository, presenter);

        CreateOfferInputData inputData = new CreateOfferInputData("Babysitting", "");

        interactor.execute(inputData);

        assertNotNull(presenter.successData);
        assertEquals("Babysitting", presenter.successData.getOffer().getTitle());
        assertEquals(1, repository.allOffers().size());
    }

    @Test
    void execute_missingTitleFails() {
        InMemoryOfferDataAccessObject repository = new InMemoryOfferDataAccessObject();
        CreateOfferPresenterSpy presenter = new CreateOfferPresenterSpy();
        CreateOfferInteractor interactor = new CreateOfferInteractor(repository, presenter);

        CreateOfferInputData inputData = new CreateOfferInputData("", "Some details");

        interactor.execute(inputData);

        assertNull(presenter.successData);
        assertNotNull(presenter.failError);
        assertEquals("Please enter a title", presenter.failError); // Or whatever your error msg is

        assertTrue(repository.allOffers().isEmpty());
    }

    @Test
    void execute_noUserLoggedInPreventsCreation() {
        UserSession.getInstance().setCurrentUser(null);

        InMemoryOfferDataAccessObject repository = new InMemoryOfferDataAccessObject();
        CreateOfferPresenterSpy presenter = new CreateOfferPresenterSpy();
        CreateOfferInteractor interactor = new CreateOfferInteractor(repository, presenter);

        interactor.execute(new CreateOfferInputData("Title", "Details"));

        assertTrue(repository.allOffers().isEmpty());
    }

    @Test
    void execute_nullTitleFails() {
        InMemoryOfferDataAccessObject repository = new InMemoryOfferDataAccessObject();
        CreateOfferPresenterSpy presenter = new CreateOfferPresenterSpy();
        CreateOfferInteractor interactor = new CreateOfferInteractor(repository, presenter);

        CreateOfferInputData inputData = new CreateOfferInputData(null, "Details but no title");

        interactor.execute(inputData);

        assertNull(presenter.successData);
        assertEquals("Please enter a title", presenter.failError);
    }

    private static final class CreateOfferPresenterSpy implements CreateOfferOutputBoundary {
        CreateOfferOutputData successData;
        String failError;

        @Override
        public void prepareSuccessView(CreateOfferOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.failError = error;
        }
    }
}
