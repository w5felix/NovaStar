package application.controllers;

import java.util.List;

import domain.entities.PortfolioEntry;
import domain.entities.User;
import application.interactors.get_current_value.GetCurrentValueInteractor;
import application.interactors.get_initial_investment.GetInitialInvestmentInteractor;
import application.interactors.get_portfolio_entries.GetPortfolioEntriesInteractor;
import views.PortfolioView;

public class PortfolioController {

    private final GetPortfolioEntriesInteractor getPortfolioEntriesInteractor;
    private final GetCurrentValueInteractor getCurrentValueInteractor;
    private final GetInitialInvestmentInteractor getInitialInvestmentInteractor;
    private final User currentUser;
    private final PortfolioView portfolioView;

    public PortfolioController(GetPortfolioEntriesInteractor getPortfolioEntriesInteractor,
                               GetCurrentValueInteractor getCurrentValueInteractor,
                               GetInitialInvestmentInteractor getInitialInvestmentInteractor,
                               User currentUser,
                               PortfolioView portfolioView) {
        this.getPortfolioEntriesInteractor = getPortfolioEntriesInteractor;
        this.getCurrentValueInteractor = getCurrentValueInteractor;
        this.getInitialInvestmentInteractor = getInitialInvestmentInteractor;
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
            // Fetch portfolio entries using the interactor
            final List<PortfolioEntry> portfolioEntries = getPortfolioEntriesInteractor.execute(currentUser.getUserId());

            // Create a new PortfolioDataHandler each time refresh is called
            final PortfolioView.PortfolioDataHandler dataHandler = new PortfolioView.PortfolioDataHandler() {
                @Override
                public double getCurrentValue(PortfolioEntry entry) {
                    try {
                        // Fetch the latest current value for the entry
                        return getCurrentValueInteractor.execute(entry);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return 0.0; // Default to 0.0 on error
                    }
                }

                @Override
                public double getInitialInvestment(PortfolioEntry entry) {
                    try {
                        // Fetch the latest initial investment for the entry
                        return getInitialInvestmentInteractor.execute(currentUser, entry);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return 0.0; // Default to 0.0 on error
                    }
                }
            };

            // Update the portfolio view with the latest portfolio and data handler
            portfolioView.updatePortfolio(portfolioEntries, dataHandler);

        } catch (Exception exception) {
            portfolioView.showError("Error refreshing portfolio: " + exception.getMessage());
        }
    }

}
