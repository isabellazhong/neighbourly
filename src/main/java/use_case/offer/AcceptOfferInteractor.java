package use_case.offer; 

import java.io.IOException;
import java.util.UUID;

import database.MongoDBOfferDataAccessObject;
import entity.SendbirdMessagingService;
import use_case.offer.accept_offer.AcceptOfferOutputBoundary;
import use_case.offer.accept_offer.AcceptOfferOutputData;


public class AcceptOfferInteractor {
    private final MongoDBOfferDataAccessObject offerDAO;
    private final SendbirdMessagingService messagingService;
    private final AcceptOfferOutputBoundary messagingPresenter;

    public AcceptOfferInteractor(
            MongoDBOfferDataAccessObject offerDAO,
            SendbirdMessagingService messagingService,
            AcceptOfferOutputBoundary outputBoundary) {
        this.offerDAO = offerDAO;
        this.messagingService = messagingService;
        this.messagingPresenter = outputBoundary;
    }

    public void execute(UUID offerId, String offererUserId, String accepterUserId) {
        try {
            String channelId = messagingService.createChannel(offererUserId, accepterUserId);

            offerDAO.setChatChannelId(offerId, channelId);
            offerDAO.setAccepted(offerId, true);

            AcceptOfferOutputData outputData = new AcceptOfferOutputData(channelId, accepterUserId);
            messagingPresenter.presentMessagingView(outputData);

        } catch (IOException e) {
            String errorMsg = "Failed to create messaging channel: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            messagingPresenter.presentFailure(errorMsg);
        } catch (Exception e) {
            String errorMsg = "Error accepting offer: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            messagingPresenter.presentFailure(errorMsg);
        }
    }
}

