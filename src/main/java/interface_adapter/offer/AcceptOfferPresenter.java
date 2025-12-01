package interface_adapter.offer;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import interface_adapter.ViewManagerModel;
import interface_adapter.messaging.MessagingState;
import interface_adapter.messaging.MessagingViewModel;
import use_case.offer.accept_offer.AcceptOfferOutputBoundary;
import use_case.offer.accept_offer.AcceptOfferOutputData;

public class AcceptOfferPresenter implements AcceptOfferOutputBoundary {
    private final MessagingViewModel messagingViewModel;
    private final ViewManagerModel viewManagerModel;
    private final JFrame parentFrame;

    public AcceptOfferPresenter(MessagingViewModel messagingViewModel,
            ViewManagerModel viewManagerModel,
            JFrame parentFrame) {
        this.messagingViewModel = messagingViewModel;
        this.viewManagerModel = viewManagerModel;
        this.parentFrame = parentFrame;
    }

    @Override
    public void presentMessagingView(AcceptOfferOutputData outputData) {
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
