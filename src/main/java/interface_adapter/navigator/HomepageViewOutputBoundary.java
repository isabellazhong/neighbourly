package interface_adapter.navigator;

import interface_adapter.home.HomepageViewModel;

public interface HomepageViewOutputBoundary {
    void prepareOfferPage();
    void prepareRequestPage();
    void refreshHomepageState(HomepageViewModel viewModel);
}
