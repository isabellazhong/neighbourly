package offer;

import app.UserSession;
import entity.Offer;
import entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import offer.InMemoryOfferDataAccessObject;
import use_case.offers.get_offers.MyOffersInteractor;
import use_case.offers.get_offers.MyOffersOutputBoundary;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MyOffersTest {

    @AfterEach
    void tearDown() {
        UserSession.getInstance().setCurrentUser(null);
    }

    @Test
    void execute_returnsOnlyOffersForCurrentUser() {
        InMemoryOfferDataAccessObject repository = new InMemoryOfferDataAccessObject();

        User userA = new User(UUID.randomUUID(), "Alice", "A", "alice@example.com", "FEMALE");
        User userB = new User(UUID.randomUUID(), "Bob", "B", "bob@example.com", "MALE");

        Offer offerA1 = new Offer("Alice's Offer", "Details", new Date());
        offerA1.setUserID(userA.getID());
        repository.addOffer(offerA1);

        Offer offerB1 = new Offer("Bob's Offer", "Details", new Date());
        offerB1.setUserID(userB.getID());
        repository.addOffer(offerB1);

        UserSession.getInstance().setCurrentUser(userA);

        MyOffersPresenterSpy presenter = new MyOffersPresenterSpy();
        MyOffersInteractor interactor = new MyOffersInteractor(repository, presenter);

        interactor.execute();

        assertEquals(1, presenter.presentedOffers.size());
        assertEquals("Alice's Offer", presenter.presentedOffers.get(0).getTitle());
        assertFalse(presenter.presentedOffers.contains(offerB1));
    }

    @Test
    void execute_returnsEmptyListIfNoOffersFound() {
        InMemoryOfferDataAccessObject repository = new InMemoryOfferDataAccessObject();
        User user = new User(UUID.randomUUID(), "Empty", "User", "empty@example.com", "MALE");
        UserSession.getInstance().setCurrentUser(user);

        MyOffersPresenterSpy presenter = new MyOffersPresenterSpy();
        MyOffersInteractor interactor = new MyOffersInteractor(repository, presenter);

        interactor.execute();

        assertTrue(presenter.presentedOffers.isEmpty());
    }

    @Test
    void execute_failUserNotLoggedIn() {
        UserSession.getInstance().setCurrentUser(null);

        InMemoryOfferDataAccessObject repository = new InMemoryOfferDataAccessObject();
        MyOffersPresenterSpy presenter = new MyOffersPresenterSpy();
        MyOffersInteractor interactor = new MyOffersInteractor(repository, presenter);

        interactor.execute();

        assertNull(presenter.presentedOffers);
        assertNotNull(presenter.failError);
        assertEquals("No user logged in", presenter.failError);
    }

    private static final class MyOffersPresenterSpy implements MyOffersOutputBoundary {
        List<Offer> presentedOffers;
        String failError;

        @Override
        public void presentMyOffers(List<Offer> offers) {
            this.presentedOffers = offers;
        }

        @Override
        public void prepareFailView(String error) {
            this.failError = error;
        }
    }
}