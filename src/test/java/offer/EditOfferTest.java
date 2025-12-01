package offer;

import entity.Offer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.offers.edit_offers.*;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EditOfferTest {

    private InMemoryOfferDataAccessObject repository;
    private UUID offerId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOfferDataAccessObject();

        offerId = UUID.randomUUID();
        Offer originalOffer = new Offer("Old Title", "Old Details", new Date());

        repository.addOffer(originalOffer);
        this.offerId = originalOffer.getId();
    }

    @Test
    void execute_successfulEditUpdatesDatabase() {
        EditOfferPresenterSpy presenter = new EditOfferPresenterSpy();
        EditOfferInteractor interactor = new EditOfferInteractor(repository, presenter);

        EditOfferInputData inputData = new EditOfferInputData(
                offerId,
                "New Title",
                "New Details"
        );

        interactor.execute(inputData);

        assertNotNull(presenter.successData);
        assertEquals("New Title", presenter.successData.getOffer().getTitle());

        Offer updatedOffer = repository.getOfferByID(offerId);
        assertEquals("New Title", updatedOffer.getTitle());
        assertEquals("New Details", updatedOffer.getAlternativeDetails());
    }

    @Test
    void execute_emptyTitleFailsValidation() {
        EditOfferPresenterSpy presenter = new EditOfferPresenterSpy();
        EditOfferInteractor interactor = new EditOfferInteractor(repository, presenter);

        EditOfferInputData inputData = new EditOfferInputData(
                offerId,
                "",
                "Details are fine"
        );

        interactor.execute(inputData);

        assertNull(presenter.successData);
        assertEquals("Title cannot be empty.", presenter.failError);

        Offer storedOffer = repository.getOfferByID(offerId);
        assertEquals("Old Title", storedOffer.getTitle());
    }

    @Test
    void execute_offerNotFoundFails() {
        EditOfferPresenterSpy presenter = new EditOfferPresenterSpy();
        EditOfferInteractor interactor = new EditOfferInteractor(repository, presenter);

        EditOfferInputData inputData = new EditOfferInputData(
                UUID.randomUUID(),
                "Title",
                "Details"
        );

        interactor.execute(inputData);

        assertEquals("Offer not found.", presenter.failError);
    }

    @Test
    void execute_nullTitleFailsValidation() {
        EditOfferPresenterSpy presenter = new EditOfferPresenterSpy();
        EditOfferInteractor interactor = new EditOfferInteractor(repository, presenter);

        EditOfferInputData inputData = new EditOfferInputData(
                offerId,
                null,
                "Details are fine"
        );

        interactor.execute(inputData);

        assertNull(presenter.successData);
        assertEquals("Title cannot be empty.", presenter.failError);
    }

    private static final class EditOfferPresenterSpy implements EditOfferOutputBoundary {
        EditOfferOutputData successData;
        String failError;

        @Override
        public void prepareSuccessView(EditOfferOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.failError = error;
        }
    }
}
