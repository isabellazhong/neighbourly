package use_case.messaging;

import java.io.IOException;
import java.util.List;

import entity.MessageDTO;
import entity.SendbirdMessagingService;
import use_case.offer.OfferDataAccessInterface;
import use_case.request.RequestDataAccessInterface;

public class MessagingInteractor implements MessagingInputBoundary {
    private final MessagingOutputBoundary messagingOutputBoundary;
    private final SendbirdMessagingService sendbirdMessagingService;
    private final RequestDataAccessInterface requestDAO;
    private final use_case.offer.OfferDataAccessInterface offerDAO;
    
    public MessagingInteractor(
            MessagingOutputBoundary messagingOutputBoundary,
            SendbirdMessagingService sendbirdMessagingService,
            RequestDataAccessInterface requestDAO,
            OfferDataAccessInterface offerDAO) {
        this.messagingOutputBoundary = messagingOutputBoundary;
        this.sendbirdMessagingService = sendbirdMessagingService;
        this.requestDAO = requestDAO;
        this.offerDAO = offerDAO;
    }
    
    @Override
    public void openChat(MessagingInputData inputData) {
        try {
            String channelId;
            
            if (inputData.isRequest()) {
                // Get channelId from Request
                channelId = requestDAO.getChatChannelId(inputData.getRequestId());
                
                // If channel doesn't exist, the request hasn't been accepted yet
                if (channelId == null || channelId.isEmpty()) {
                    messagingOutputBoundary.prepareFailView("Channel not found for this request.");
                    return;
                }
            } else {
                // Get channelId from Offer
                channelId = offerDAO.getChatChannelId(inputData.getOfferId());
                
                // If channel doesn't exist, the offer hasn't been accepted yet
                if (channelId == null || channelId.isEmpty()) {
                    messagingOutputBoundary.prepareFailView("Channel not found for this offer.");
                    return;
                }
            }
            
            // Fetch messages for the channel
            List<MessageDTO> messages = sendbirdMessagingService.fetchMessages(channelId);
            
            MessagingOutputData outputData = new MessagingOutputData(messages, channelId);
            messagingOutputBoundary.prepareSuccessView(outputData);
            
        } catch (IOException e) {
            messagingOutputBoundary.prepareFailView("Failed to open chat: " + e.getMessage());
        } catch (Exception e) {
            messagingOutputBoundary.prepareFailView("Error opening chat: " + e.getMessage());
        }
    }
    
    @Override
    public void sendMessage(MessagingInputData inputData) {
        try {
            sendbirdMessagingService.sendMessage(
                inputData.getChannelId(),
                inputData.getSenderId(),
                inputData.getMessageText()
            );
            
            // Fetch updated messages
            List<MessageDTO> messages = sendbirdMessagingService.fetchMessages(inputData.getChannelId());
            
            MessagingOutputData outputData = new MessagingOutputData(messages, inputData.getChannelId());
            messagingOutputBoundary.prepareSuccessView(outputData);
            
        } catch (IOException e) {
            messagingOutputBoundary.prepareFailView("Failed to send message: " + e.getMessage());
        } catch (Exception e) {
            messagingOutputBoundary.prepareFailView("Error sending message: " + e.getMessage());
        }
    }
    
    @Override
    public void refreshMessages(MessagingInputData inputData) {
        try {
            List<MessageDTO> messages;
            
            // If lastTimestamp is 0, fetch all messages; otherwise poll for new ones
            if (inputData.getLastTimestamp() == 0) {
                messages = sendbirdMessagingService.fetchMessages(inputData.getChannelId());
            } else {
                messages = sendbirdMessagingService.pollNewMessages(
                    inputData.getChannelId(),
                    inputData.getLastTimestamp()
                );
            }
            
            MessagingOutputData outputData = new MessagingOutputData(messages, inputData.getChannelId());
            messagingOutputBoundary.prepareSuccessView(outputData);
            
        } catch (IOException e) {
            messagingOutputBoundary.prepareFailView("Failed to refresh messages: " + e.getMessage());
        } catch (Exception e) {
            messagingOutputBoundary.prepareFailView("Error refreshing messages: " + e.getMessage());
        }
    }
}
