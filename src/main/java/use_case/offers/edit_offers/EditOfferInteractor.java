package use_case.offers.edit_offers;
import entity.Offer;
import use_case.offers.create_offer.OfferDataAccessInterface;

public class EditOfferInteractor implements EditOfferInputBoundary {
    final OfferDataAccessInterface dao;
    final EditOfferOutputBoundary presenter;

    public EditOfferInteractor(OfferDataAccessInterface dao, EditOfferOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditOfferInputData inputData) {
        if (inputData.getTitle() == null || inputData.getTitle().trim().isEmpty()) {
            presenter.prepareFailView("Title cannot be empty.");
            return;
        }

        Offer offer = dao.getOfferByID(inputData.getOfferId());

        if (offer == null) {
            presenter.prepareFailView("Offer not found.");
            return;
        }

        offer.setTitle(inputData.getTitle());
        offer.setAlternativeDetails(inputData.getDetails());

        dao.updateOffer(offer);

        presenter.prepareSuccessView(new EditOfferOutputData(offer));
    }
}
