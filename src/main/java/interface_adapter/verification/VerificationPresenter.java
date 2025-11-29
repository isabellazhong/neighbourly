package interface_adapter.verification;
import interface_adapter.ViewManagerModel;
import use_case.start.id_verification.*;
import view.homepage.HomepageView;;

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
        verificationViewModel.setState(state);
        verificationViewModel.firePropertyChange();

        viewManagerModel.setState(verificationViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
