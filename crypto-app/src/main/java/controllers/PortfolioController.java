package controllers;

import java.util.List;

import entities.PortfolioEntry;
import entities.User;
import interactors.UserService;
import views.PortfolioView;

public class PortfolioController {

    private final UserService userService;
    private final User currentUser;
    private final PortfolioView portfolioView;

    public PortfolioController(UserService userService, User currentUser, PortfolioView portfolioView) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.portfolioView = portfolioView;

        refresh();
    }

    public PortfolioView getView() {
        return portfolioView;
    }

    /**
     * Refresh function.
     */
    public void refresh() {
        try {
            // Fetch portfolio entries
            final List<PortfolioEntry> portfolioEntries = userService.getPortfolioEntries(currentUser);

            // Provide the PortfolioDataHandler implementation
            final PortfolioView.PortfolioDataHandler dataHandler = new PortfolioView.PortfolioDataHandler() {
                @Override
                public double getCurrentValue(PortfolioEntry entry) {
                    try {
                        return userService.getCurrentValue(entry);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        return 0.0;
                    }
                }

                @Override
                public double getInitialInvestment(PortfolioEntry entry) {
                    try {
                        return userService.getInitialInvestment(currentUser, entry);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        return 0.0;
                    }
                }
            };

            // Pass data and handler to the view
            portfolioView.updatePortfolio(portfolioEntries, dataHandler);

        }
        catch (Exception exception) {
            portfolioView.showError("Error refreshing portfolio: " + exception.getMessage());
        }
    }
}
