package interface_adapter.messaging;

import interface_adapter.ViewModel;

public class MessagingViewModel extends ViewModel<MessagingState> {
    public MessagingViewModel() {
        super("messaging");
        setState(new MessagingState());
    }
}
