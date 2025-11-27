package messaging.interface_adapter;

import messaging.use_case.MessagingOutputBoundary;
import messaging.use_case.MessagingOutputData;
import messaging.use_case.MessagingState;

public class MessagingPresenter implements MessagingOutputBoundary {
    private final MessagingViewModel messagingViewModel;

    public MessagingPresenter(MessagingViewModel messagingViewModel) {
        this.messagingViewModel = messagingViewModel;
    }

    @Override
    public void prepareSuccessView(MessagingOutputData outputData) {
        MessagingState state = messagingViewModel.getState();
        state.setChannelId(outputData.getChannelId());
        state.setMessages(outputData.getMessages());
        state.setError(null);
        messagingViewModel.setState(state);
        messagingViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        MessagingState state = messagingViewModel.getState();
        state.setError(error);
        messagingViewModel.setState(state);
        messagingViewModel.firePropertyChange();
    }
}
