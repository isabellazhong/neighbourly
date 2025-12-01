package use_case.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import database.OfferDataAccessInterface;
import database.RequestDataAccessInterface;
import entity.MessageDTO;
import entity.SendbirdMessagingService;

@ExtendWith(MockitoExtension.class)
class MessagingInteractorTest {

    @Mock
    private MessagingOutputBoundary messagingOutputBoundary;

    @Mock
    private SendbirdMessagingService sendbirdMessagingService;

    @Mock
    private RequestDataAccessInterface requestDAO;

    @Mock
    private OfferDataAccessInterface offerDAO;

    private MessagingInteractor messagingInteractor;

    private UUID testRequestId;
    private UUID testOfferId;
    private String testChannelId;
    private String testUserId;
    private List<MessageDTO> testMessages;

    @BeforeEach
    void setUp() {
        testRequestId = UUID.randomUUID();
        testOfferId = UUID.randomUUID();
        testChannelId = "test-channel-123";
        testUserId = "user-123";

        // Create test message list
        testMessages = new ArrayList<>();
        testMessages.add(new MessageDTO("msg-1", "user-1", "Hello", 1000L));
        testMessages.add(new MessageDTO("msg-2", "user-2", "Hi there", 2000L));

        // Manually construct MessagingInteractor with mocked dependencies
        messagingInteractor = new MessagingInteractor(
                messagingOutputBoundary,
                sendbirdMessagingService,
                requestDAO,
                offerDAO
        );
    }

    // ============================================
    // openChat() Tests
    // ============================================

    @Test
    void testOpenChat_Request_ChannelExists_Success() throws IOException {
        // Arrange
        MessagingInputData inputData = MessagingInputData.forOpenChat(
                testRequestId, null, testUserId, true);

        when(requestDAO.getChatChannelId(testRequestId)).thenReturn(testChannelId);
        when(sendbirdMessagingService.fetchMessages(testChannelId)).thenReturn(testMessages);

        ArgumentCaptor<MessagingOutputData> outputDataCaptor = ArgumentCaptor.forClass(MessagingOutputData.class);

        // Act
        messagingInteractor.openChat(inputData);

        // Assert
        verify(requestDAO, times(1)).getChatChannelId(testRequestId);
        verify(offerDAO, never()).getChatChannelId(any());
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(messagingOutputBoundary, times(1)).prepareSuccessView(outputDataCaptor.capture());
        
        MessagingOutputData capturedOutput = outputDataCaptor.getValue();
        assertEquals(testChannelId, capturedOutput.getChannelId());
        assertEquals(testMessages, capturedOutput.getMessages());
        verify(messagingOutputBoundary, never()).prepareFailView(anyString());
    }

    @Test
    void testOpenChat_Offer_ChannelExists_Success() throws IOException {
        // Arrange
        MessagingInputData inputData = MessagingInputData.forOpenChat(
                null, testOfferId, testUserId, false);

        when(offerDAO.getChatChannelId(testOfferId)).thenReturn(testChannelId);
        when(sendbirdMessagingService.fetchMessages(testChannelId)).thenReturn(testMessages);

        ArgumentCaptor<MessagingOutputData> outputDataCaptor = ArgumentCaptor.forClass(MessagingOutputData.class);

        // Act
        messagingInteractor.openChat(inputData);

        // Assert
        verify(offerDAO, times(1)).getChatChannelId(testOfferId);
        verify(requestDAO, never()).getChatChannelId(any());
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(messagingOutputBoundary, times(1)).prepareSuccessView(outputDataCaptor.capture());
        
        MessagingOutputData capturedOutput = outputDataCaptor.getValue();
        assertEquals(testChannelId, capturedOutput.getChannelId());
        assertEquals(testMessages, capturedOutput.getMessages());
        verify(messagingOutputBoundary, never()).prepareFailView(anyString());
    }

    @Test
    void testOpenChat_Request_ChannelNull_Fail() {
        // Arrange
        MessagingInputData inputData = MessagingInputData.forOpenChat(
                testRequestId, null, testUserId, true);

        when(requestDAO.getChatChannelId(testRequestId)).thenReturn(null);

        // Act
        messagingInteractor.openChat(inputData);

        // Assert
        verify(requestDAO, times(1)).getChatChannelId(testRequestId);
        verifyNoInteractions(sendbirdMessagingService);
        verify(messagingOutputBoundary, times(1)).prepareFailView("Channel not found for this request.");
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testOpenChat_Request_ChannelEmpty_Fail() {
        // Arrange
        MessagingInputData inputData = MessagingInputData.forOpenChat(
                testRequestId, null, testUserId, true);

        when(requestDAO.getChatChannelId(testRequestId)).thenReturn("");

        // Act
        messagingInteractor.openChat(inputData);

        // Assert
        verify(requestDAO, times(1)).getChatChannelId(testRequestId);
        verifyNoInteractions(sendbirdMessagingService);
        verify(messagingOutputBoundary, times(1)).prepareFailView("Channel not found for this request.");
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testOpenChat_Offer_ChannelNull_Fail() {
        // Arrange
        MessagingInputData inputData = MessagingInputData.forOpenChat(
                null, testOfferId, testUserId, false);

        when(offerDAO.getChatChannelId(testOfferId)).thenReturn(null);

        // Act
        messagingInteractor.openChat(inputData);

        // Assert
        verify(offerDAO, times(1)).getChatChannelId(testOfferId);
        verifyNoInteractions(sendbirdMessagingService);
        verify(messagingOutputBoundary, times(1)).prepareFailView("Channel not found for this offer.");
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testOpenChat_Offer_ChannelEmpty_Fail() {
        // Arrange
        MessagingInputData inputData = MessagingInputData.forOpenChat(
                null, testOfferId, testUserId, false);

        when(offerDAO.getChatChannelId(testOfferId)).thenReturn("");

        // Act
        messagingInteractor.openChat(inputData);

        // Assert
        verify(offerDAO, times(1)).getChatChannelId(testOfferId);
        verifyNoInteractions(sendbirdMessagingService);
        verify(messagingOutputBoundary, times(1)).prepareFailView("Channel not found for this offer.");
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testOpenChat_Request_IOException_Fail() throws IOException {
        // Arrange
        MessagingInputData inputData = MessagingInputData.forOpenChat(
                testRequestId, null, testUserId, true);
        String errorMessage = "Network error";

        when(requestDAO.getChatChannelId(testRequestId)).thenReturn(testChannelId);
        when(sendbirdMessagingService.fetchMessages(testChannelId))
                .thenThrow(new IOException(errorMessage));

        // Act
        messagingInteractor.openChat(inputData);

        // Assert
        verify(requestDAO, times(1)).getChatChannelId(testRequestId);
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(messagingOutputBoundary, times(1)).prepareFailView("Failed to open chat: " + errorMessage);
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testOpenChat_Request_GenericException_Fail() throws IOException {
        // Arrange
        MessagingInputData inputData = MessagingInputData.forOpenChat(
                testRequestId, null, testUserId, true);
        String errorMessage = "Unexpected error";

        when(requestDAO.getChatChannelId(testRequestId)).thenReturn(testChannelId);
        when(sendbirdMessagingService.fetchMessages(testChannelId))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        messagingInteractor.openChat(inputData);

        // Assert
        verify(requestDAO, times(1)).getChatChannelId(testRequestId);
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(messagingOutputBoundary, times(1)).prepareFailView("Error opening chat: " + errorMessage);
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    // ============================================
    // sendMessage() Tests
    // ============================================

    @Test
    void testSendMessage_Success() throws IOException {
        // Arrange
        String senderId = "sender-123";
        String messageText = "Test message";
        MessagingInputData inputData = MessagingInputData.forSendMessage(
                testChannelId, senderId, messageText);

        doNothing().when(sendbirdMessagingService).sendMessage(
                testChannelId, senderId, messageText);
        when(sendbirdMessagingService.fetchMessages(testChannelId)).thenReturn(testMessages);

        ArgumentCaptor<MessagingOutputData> outputDataCaptor = ArgumentCaptor.forClass(MessagingOutputData.class);

        // Act
        messagingInteractor.sendMessage(inputData);

        // Assert
        verify(sendbirdMessagingService, times(1)).sendMessage(
                testChannelId, senderId, messageText);
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(messagingOutputBoundary, times(1)).prepareSuccessView(outputDataCaptor.capture());
        
        MessagingOutputData capturedOutput = outputDataCaptor.getValue();
        assertEquals(testChannelId, capturedOutput.getChannelId());
        assertEquals(testMessages, capturedOutput.getMessages());
        verify(messagingOutputBoundary, never()).prepareFailView(anyString());
    }

    @Test
    void testSendMessage_IOException_Fail() throws IOException {
        // Arrange
        String senderId = "sender-123";
        String messageText = "Test message";
        String errorMessage = "Send failed";
        MessagingInputData inputData = MessagingInputData.forSendMessage(
                testChannelId, senderId, messageText);

        doThrow(new IOException(errorMessage)).when(sendbirdMessagingService)
                .sendMessage(testChannelId, senderId, messageText);

        // Act
        messagingInteractor.sendMessage(inputData);

        // Assert
        verify(sendbirdMessagingService, times(1)).sendMessage(
                testChannelId, senderId, messageText);
        verify(sendbirdMessagingService, never()).fetchMessages(anyString());
        verify(messagingOutputBoundary, times(1)).prepareFailView("Failed to send message: " + errorMessage);
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testSendMessage_FetchMessagesIOException_Fail() throws IOException {
        // Arrange
        String senderId = "sender-123";
        String messageText = "Test message";
        String errorMessage = "Fetch failed";
        MessagingInputData inputData = MessagingInputData.forSendMessage(
                testChannelId, senderId, messageText);

        doNothing().when(sendbirdMessagingService).sendMessage(
                testChannelId, senderId, messageText);
        when(sendbirdMessagingService.fetchMessages(testChannelId))
                .thenThrow(new IOException(errorMessage));

        // Act
        messagingInteractor.sendMessage(inputData);

        // Assert
        verify(sendbirdMessagingService, times(1)).sendMessage(
                testChannelId, senderId, messageText);
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(messagingOutputBoundary, times(1)).prepareFailView("Failed to send message: " + errorMessage);
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testSendMessage_GenericException_Fail() throws IOException {
        // Arrange
        String senderId = "sender-123";
        String messageText = "Test message";
        String errorMessage = "Unexpected error";
        MessagingInputData inputData = MessagingInputData.forSendMessage(
                testChannelId, senderId, messageText);

        doThrow(new RuntimeException(errorMessage)).when(sendbirdMessagingService)
                .sendMessage(testChannelId, senderId, messageText);

        // Act
        messagingInteractor.sendMessage(inputData);

        // Assert
        verify(sendbirdMessagingService, times(1)).sendMessage(
                testChannelId, senderId, messageText);
        verify(sendbirdMessagingService, never()).fetchMessages(anyString());
        verify(messagingOutputBoundary, times(1)).prepareFailView("Error sending message: " + errorMessage);
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    // ============================================
    // refreshMessages() Tests
    // ============================================

    @Test
    void testRefreshMessages_LastTimestampZero_UsesFetchMessages() throws IOException {
        // Arrange
        long lastTimestamp = 0L;
        MessagingInputData inputData = MessagingInputData.forRefresh(testChannelId, lastTimestamp);

        when(sendbirdMessagingService.fetchMessages(testChannelId)).thenReturn(testMessages);

        ArgumentCaptor<MessagingOutputData> outputDataCaptor = ArgumentCaptor.forClass(MessagingOutputData.class);

        // Act
        messagingInteractor.refreshMessages(inputData);

        // Assert
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(sendbirdMessagingService, never()).pollNewMessages(anyString(), anyLong());
        verify(messagingOutputBoundary, times(1)).prepareSuccessView(outputDataCaptor.capture());
        
        MessagingOutputData capturedOutput = outputDataCaptor.getValue();
        assertEquals(testChannelId, capturedOutput.getChannelId());
        assertEquals(testMessages, capturedOutput.getMessages());
        verify(messagingOutputBoundary, never()).prepareFailView(anyString());
    }

    @Test
    void testRefreshMessages_LastTimestampGreaterThanZero_UsesPollNewMessages() throws IOException {
        // Arrange
        long lastTimestamp = 5000L;
        MessagingInputData inputData = MessagingInputData.forRefresh(testChannelId, lastTimestamp);

        when(sendbirdMessagingService.pollNewMessages(testChannelId, lastTimestamp))
                .thenReturn(testMessages);

        ArgumentCaptor<MessagingOutputData> outputDataCaptor = ArgumentCaptor.forClass(MessagingOutputData.class);

        // Act
        messagingInteractor.refreshMessages(inputData);

        // Assert
        verify(sendbirdMessagingService, never()).fetchMessages(anyString());
        verify(sendbirdMessagingService, times(1)).pollNewMessages(testChannelId, lastTimestamp);
        verify(messagingOutputBoundary, times(1)).prepareSuccessView(outputDataCaptor.capture());
        
        MessagingOutputData capturedOutput = outputDataCaptor.getValue();
        assertEquals(testChannelId, capturedOutput.getChannelId());
        assertEquals(testMessages, capturedOutput.getMessages());
        verify(messagingOutputBoundary, never()).prepareFailView(anyString());
    }

    @Test
    void testRefreshMessages_FetchMessages_IOException_Fail() throws IOException {
        // Arrange
        long lastTimestamp = 0L;
        String errorMessage = "Fetch error";
        MessagingInputData inputData = MessagingInputData.forRefresh(testChannelId, lastTimestamp);

        when(sendbirdMessagingService.fetchMessages(testChannelId))
                .thenThrow(new IOException(errorMessage));

        // Act
        messagingInteractor.refreshMessages(inputData);

        // Assert
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(sendbirdMessagingService, never()).pollNewMessages(anyString(), anyLong());
        verify(messagingOutputBoundary, times(1)).prepareFailView("Failed to refresh messages: " + errorMessage);
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testRefreshMessages_PollNewMessages_IOException_Fail() throws IOException {
        // Arrange
        long lastTimestamp = 5000L;
        String errorMessage = "Poll error";
        MessagingInputData inputData = MessagingInputData.forRefresh(testChannelId, lastTimestamp);

        when(sendbirdMessagingService.pollNewMessages(testChannelId, lastTimestamp))
                .thenThrow(new IOException(errorMessage));

        // Act
        messagingInteractor.refreshMessages(inputData);

        // Assert
        verify(sendbirdMessagingService, never()).fetchMessages(anyString());
        verify(sendbirdMessagingService, times(1)).pollNewMessages(testChannelId, lastTimestamp);
        verify(messagingOutputBoundary, times(1)).prepareFailView("Failed to refresh messages: " + errorMessage);
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testRefreshMessages_GenericException_Fail() throws IOException {
        // Arrange
        long lastTimestamp = 0L;
        String errorMessage = "Unexpected error";
        MessagingInputData inputData = MessagingInputData.forRefresh(testChannelId, lastTimestamp);

        when(sendbirdMessagingService.fetchMessages(testChannelId))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        messagingInteractor.refreshMessages(inputData);

        // Assert
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(sendbirdMessagingService, never()).pollNewMessages(anyString(), anyLong());
        verify(messagingOutputBoundary, times(1)).prepareFailView("Error refreshing messages: " + errorMessage);
        verify(messagingOutputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testRefreshMessages_EmptyMessagesList_Success() throws IOException {
        // Arrange
        long lastTimestamp = 0L;
        List<MessageDTO> emptyMessages = new ArrayList<>();
        MessagingInputData inputData = MessagingInputData.forRefresh(testChannelId, lastTimestamp);

        when(sendbirdMessagingService.fetchMessages(testChannelId)).thenReturn(emptyMessages);

        ArgumentCaptor<MessagingOutputData> outputDataCaptor = ArgumentCaptor.forClass(MessagingOutputData.class);

        // Act
        messagingInteractor.refreshMessages(inputData);

        // Assert
        verify(sendbirdMessagingService, times(1)).fetchMessages(testChannelId);
        verify(messagingOutputBoundary, times(1)).prepareSuccessView(outputDataCaptor.capture());
        
        MessagingOutputData capturedOutput = outputDataCaptor.getValue();
        assertEquals(testChannelId, capturedOutput.getChannelId());
        assertTrue(capturedOutput.getMessages().isEmpty());
        verify(messagingOutputBoundary, never()).prepareFailView(anyString());
    }
}

