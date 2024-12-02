package application.interactors.get_transactions;

import domain.entities.Transaction;
import domain.entities.User;
import infrastructure.interfaces.UserApi;

import java.util.List;

public class GetTransactionsInteractor {
    private final UserApi userApi;

    public GetTransactionsInteractor(UserApi userApi) {
        this.userApi = userApi;
    }

    /**
     * Fetches transactions for the given user.
     * @param user The user whose transactions are to be fetched.
     * @return List of transactions.
     * @throws Exception if fetching transactions fails.
     */
    public List<Transaction> execute(User user) throws Exception {
        return userApi.getTransactions(user.getUserId());
    }
}
