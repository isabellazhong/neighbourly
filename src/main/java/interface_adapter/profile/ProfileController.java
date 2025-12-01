package interface_adapter.profile;

import use_case.profile.ProfileInputBoundary;
import use_case.profile.ProfileInputData;

public class ProfileController {
    private final ProfileInputBoundary profileUseCaseInteractor;

    public ProfileController(ProfileInputBoundary profileUseCaseInteractor) {
        this.profileUseCaseInteractor = profileUseCaseInteractor;
    }

    public void execute(String name, String lastName, String gender) {
        ProfileInputData inputData = new ProfileInputData(name, lastName, gender);
        profileUseCaseInteractor.execute(inputData);
    }

    public void loadProfile() {
        profileUseCaseInteractor.loadProfile();
    }
}



