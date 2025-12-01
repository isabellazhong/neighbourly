package interface_adapter.profile;

import interface_adapter.profile.ProfileViewModel;
import use_case.profile.ProfileOutputBoundary;
import use_case.profile.ProfileOutputData;
import use_case.profile.ProfileState;

public class ProfilePresenter implements ProfileOutputBoundary {
    private final ProfileViewModel profileViewModel;

    public ProfilePresenter(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void prepareSuccessView(ProfileOutputData profileOutputData) {
        prepareSuccessView(profileOutputData, true);
    }

    @Override
    public void prepareSuccessView(ProfileOutputData profileOutputData, boolean dbUpdated) {
        ProfileState newState = new ProfileState();
        newState.setName(profileOutputData.getName());
        newState.setLastName(profileOutputData.getLastName());
        newState.setEmail(profileOutputData.getEmail());
        newState.setGender(profileOutputData.getGender());
        newState.setProfileError(null);
        if (!dbUpdated) {
            newState.setWarning("Warning: Database connection not available. Changes saved in memory only.");
        } else {
            newState.setWarning(null);
        }
        profileViewModel.setState(newState);
        profileViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        ProfileState state = profileViewModel.getState();
        state.setProfileError(error);
        profileViewModel.setState(state);
        profileViewModel.firePropertyChange("error");
    }
}

