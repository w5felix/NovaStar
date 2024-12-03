package application.interactors.get_portfolio_entries;

import domain.entities.PortfolioEntry;
import infrastructure.interfaces.UserApi;

import java.io.IOException;
import java.util.List;

public class GetPortfolioEntriesInteractor {
    private final UserApi userApi;

    public GetPortfolioEntriesInteractor(UserApi userApi) {
        this.userApi = userApi;
    }

    public List<PortfolioEntry> execute(String userId) throws IOException {
        return userApi.getPortfolioEntries(userId);
    }
}
