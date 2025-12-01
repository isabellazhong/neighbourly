package interface_adapter.interactor;

import interface_adapter.home.HomepageViewModel;

public interface HomepageViewOutputBoundary {
    void prepareOfferPage();
    void prepareRequestPage();
    void refreshHomepageState(HomepageViewModel viewModel);
}
