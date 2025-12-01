package use_case.profile;

import app.UserSession;
import database.MongoDB;
import entity.User;
import use_case.start.UserDataAccessInterface;

public class ProfileInteractor implements ProfileInputBoundary {
    final ProfileOutputBoundary profilePresenter;
    final UserDataAccessInterface userDataAccessObject;

    public ProfileInteractor(ProfileOutputBoundary profilePresenter, UserDataAccessInterface userDataAccessObject) {
        this.profilePresenter = profilePresenter;
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void loadProfile() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null) {
            profilePresenter.prepareFailView("No user logged in");
            return;
        }

        ProfileOutputData outputData = new ProfileOutputData(
            currentUser.getName(),
            currentUser.getLastName(),
            currentUser.getEmail(),
            currentUser.getGender()
        );
        profilePresenter.prepareSuccessView(outputData);
    }

    @Override
    public void execute(ProfileInputData profileInputData) {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null) {
            profilePresenter.prepareFailView("No user logged in");
            return;
        }

        String name = profileInputData.getName();
        String lastName = profileInputData.getLastName();
        String gender = profileInputData.getGender();

        if (name == null || name.trim().isEmpty()) {
            profilePresenter.prepareFailView("Name cannot be empty");
            return;
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            profilePresenter.prepareFailView("Last name cannot be empty");
            return;
        }

        if (gender == null || gender.trim().isEmpty()) {
            profilePresenter.prepareFailView("Gender must be selected");
            return;
        }

        currentUser.setName(name);
        currentUser.setLastName(lastName);
        currentUser.setGender(gender);

        boolean dbUpdated = userDataAccessObject.updateUser(currentUser);

        ProfileOutputData outputData = new ProfileOutputData(
            currentUser.getName(),
            currentUser.getLastName(),
            currentUser.getEmail(),
            currentUser.getGender()
        );
        profilePresenter.prepareSuccessView(outputData, dbUpdated);
    }
}

