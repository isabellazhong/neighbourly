package interface_adapter.profile;

import interface_adapter.ViewModel;
import use_case.profile.ProfileState;

public class ProfileViewModel extends ViewModel<ProfileState> {
    public ProfileViewModel() {
        super("profile");
        setState(new ProfileState());
    }
}



