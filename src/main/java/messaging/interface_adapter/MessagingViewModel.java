package messaging.interface_adapter;

import interface_adapter.ViewModel;
import messaging.use_case.MessagingState;

public class MessagingViewModel extends ViewModel<MessagingState> {
    public MessagingViewModel() {
        super("messaging");
        setState(new MessagingState());
    }
}
