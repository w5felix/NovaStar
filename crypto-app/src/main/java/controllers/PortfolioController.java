package controllers;

import entities.PortfolioEntry;
import entities.User;
import interactors.UserService;
import views.PortfolioView;

import java.util.List;

public class PortfolioController {

    private final UserService userService;
    private final User currentUser;
    private final PortfolioView portfolioView;

    public PortfolioController(UserService userService, User currentUser, PortfolioView portfolioView) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.portfolioView = portfolioView;

        refresh(); // Load data initially
    }

    public PortfolioView getView() {
        return portfolioView;
    }

    public void refresh() {
        try {
            // Fetch portfolio entries
            List<PortfolioEntry> portfolioEntries = userService.getPortfolioEntries(currentUser);

            // Provide the PortfolioDataHandler implementation
            PortfolioView.PortfolioDataHandler dataHandler = new PortfolioView.PortfolioDataHandler() {
                @Override
                public double getCurrentValue(PortfolioEntry entry) {
                    try {
                        return userService.getCurrentValue(entry);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0.0; // Return a default value in case of failure
                    }
                }

                @Override
                public double getInitialInvestment(PortfolioEntry entry) {
                    try {
                        return userService.getInitialInvestment(currentUser, entry);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0.0; // Return a default value in case of failure
                    }
                }
            };

            // Pass data and handler to the view
            portfolioView.updatePortfolio(portfolioEntries, dataHandler);

        } catch (Exception e) {
            portfolioView.showError("Error refreshing portfolio: " + e.getMessage());
        }
    }
}
