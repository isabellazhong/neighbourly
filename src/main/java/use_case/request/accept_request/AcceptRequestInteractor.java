package use_case.request.accept_request;

import database.MongoDBRequestDataAccessObject;
import entity.SendbirdMessagingService;

import java.io.IOException;
import java.util.UUID;

public class AcceptRequestInteractor {
    private final MongoDBRequestDataAccessObject requestDAO;
    private final SendbirdMessagingService messagingService;
    private final AcceptRequestOutputBoundary outputBoundary;

    public AcceptRequestInteractor(
            MongoDBRequestDataAccessObject requestDAO,
            SendbirdMessagingService messagingService,
            AcceptRequestOutputBoundary outputBoundary) {
        this.requestDAO = requestDAO;
        this.messagingService = messagingService;
        this.outputBoundary = outputBoundary;
    }

    public void execute(UUID requestId, String requesterUserId, String accepterUserId) {
        try {
            // 1. Create channel between the two users
            String channelId = messagingService.createChannel(requesterUserId, accepterUserId);

            // 2. Save channelId into request entity and mark as fulfilled
            requestDAO.setChatChannelId(requestId, channelId);
            requestDAO.setFulfilled(requestId, true);

            // 3. Persist via DAO (already done above)

            // 4. After accept completes, open MessagingView with the new channel
            AcceptRequestOutputData outputData = new AcceptRequestOutputData(channelId, accepterUserId);
            outputBoundary.presentMessagingView(outputData);

        } catch (IOException e) {
            String errorMsg = "Failed to create messaging channel: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            outputBoundary.presentFailure(errorMsg);
        } catch (Exception e) {
            String errorMsg = "Error accepting request: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            outputBoundary.presentFailure(errorMsg);
        }
    }
}

