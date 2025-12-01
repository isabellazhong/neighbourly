package interface_adapter.verification;

import interface_adapter.ViewManagerModel;
import use_case.start.id_verification.*;
import view.homepage.HomepageView;
import java.io.File;

public class VerificationPresenter implements VerificationOutputBoundary {
    private HomepageView homepageView;
    private ViewManagerModel viewManagerModel;
    private VerificationViewModel verificationViewModel;

    public VerificationPresenter(HomepageView homepageView,
            ViewManagerModel viewManagerModel,
            VerificationViewModel verificationViewModel) {
        this.homepageView = homepageView;
        this.viewManagerModel = viewManagerModel;
        this.verificationViewModel = verificationViewModel;
    }

    @Override
    public void prepareVerificationErrorView(String error) {
        VerificationViewState state = verificationViewModel.getState();
        if (state == null) {
            state = new VerificationViewState();
        }
        state.setErrorMessage(error);
        state.setVerifying(false);
        state.setStatusMessage("Upload a government-issued ID to continue.");
        verificationViewModel.setState(state);
        verificationViewModel.firePropertyChange();

        viewManagerModel.setState(verificationViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareVerficationSuccess() {
        viewManagerModel.setState(homepageView.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareVerficationSuccessButton() {
        VerificationViewState state = verificationViewModel.getState();
        state.setVerificationSuccessful(true);
        state.setVerifying(false);
        state.setStatusMessage("Verification complete.");
        verificationViewModel.setState(state);
        verificationViewModel.firePropertyChange();

        viewManagerModel.setState(verificationViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareUploadFileView(File file) {
        VerificationViewState state = verificationViewModel.getState();
        state.setSelectedFileName(file.getName());
        state.setSelectedFilePath(file.getAbsolutePath());
        state.setStatusMessage("File ready. Click verify to continue.");
        state.setErrorMessage("");
        state.resetVerificationFlags();

        verificationViewModel.setState(state);
        verificationViewModel.firePropertyChange();
    }

    @Override
    public void prepareVerifyingView() {
        VerificationViewState state = verificationViewModel.getState();
        if (state.getSelectedFilePath() == null) {
            state.setErrorMessage("Please upload a file before verifying.");
            return;
        }

        state.setVerifying(true);
        state.setStatusMessage("Verifying your document...");
        verificationViewModel.setState(state);
        verificationViewModel.firePropertyChange();
    }
}
