package interface_adapter.request;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import interface_adapter.ViewManagerModel;
import interface_adapter.messaging.MessagingState;
import interface_adapter.messaging.MessagingViewModel;
import use_case.request.accept_request.AcceptRequestOutputBoundary;
import use_case.request.accept_request.AcceptRequestOutputData;

public class AcceptRequestPresenter implements AcceptRequestOutputBoundary {
    private final MessagingViewModel messagingViewModel;
    private final ViewManagerModel viewManagerModel;
    private final JFrame parentFrame;

    public AcceptRequestPresenter(MessagingViewModel messagingViewModel,
            ViewManagerModel viewManagerModel,
            JFrame parentFrame) {
        this.messagingViewModel = messagingViewModel;
        this.viewManagerModel = viewManagerModel;
        this.parentFrame = parentFrame;
    }

    @Override
    public void presentMessagingView(AcceptRequestOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            MessagingState state = messagingViewModel.getState();
            state.setChannelId(outputData.getChannelId());
            state.setMessages(new ArrayList<>());
            state.setError(null);
            state.setCurrentUserId(outputData.getAccepterUserId());
            messagingViewModel.setState(state);
            messagingViewModel.firePropertyChange();

            viewManagerModel.setState(messagingViewModel.getViewName());
            viewManagerModel.firePropertyChange();
        });
    }

    @Override
    public void presentFailure(String errorMessage) {
        SwingUtilities.invokeLater(
                () -> JOptionPane.showMessageDialog(parentFrame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE));
    }
}
