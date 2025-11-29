package use_case.profile;

public interface ProfileOutputBoundary {
    void prepareSuccessView(ProfileOutputData profileOutputData);
    void prepareSuccessView(ProfileOutputData profileOutputData, boolean dbUpdated);
    void prepareFailView(String error);
}

