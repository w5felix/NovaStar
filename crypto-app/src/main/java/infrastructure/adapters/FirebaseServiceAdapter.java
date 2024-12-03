package infrastructure.adapters;

import infrastructure.api_clients.FireBaseApiClient;
import domain.entities.PortfolioEntry;
import domain.entities.Transaction;
import infrastructure.interfaces.UserApi;

import java.io.IOException;
import java.util.List;

/**
 * Adapter for Firebase API, implementing UserApi interface.
 */
public class FirebaseServiceAdapter implements UserApi {

    @Override
    public String loginUser(String email, String password) throws IOException {
        return FireBaseApiClient.loginUser(email, password);
    }

    @Override
    public String registerUser(String username, String email, String password, String securityQuestion, String securityAnswer) throws IOException {
        return FireBaseApiClient.registerUser(username, email, password, securityQuestion, securityAnswer);
    }

    @Override
    public void addCash(String userId, double amount) throws IOException {
        FireBaseApiClient.addCash(userId, amount);
    }

    @Override
    public void withdrawCash(String userId, double amount) throws IOException {
        FireBaseApiClient.withdrawCash(userId, amount);
    }

    @Override
    public double getCashReserves(String userId) throws IOException {
        return FireBaseApiClient.getCashReserves(userId);
    }

    @Override
    public List<Transaction> getTransactions(String userId) throws IOException {
        return FireBaseApiClient.getTransactions(userId);
    }

    @Override
    public List<PortfolioEntry> getPortfolioEntries(String userId) throws IOException {
        return FireBaseApiClient.getPortfolioEntries(userId);
    }

    @Override
    public void addTransactionAndUpdatePortfolio(String userId, Transaction transaction) throws IOException {
        FireBaseApiClient.addTransactionAndUpdatePortfolio(userId, transaction);
    }

    @Override
    public void resetPassword(String email) throws IOException {
        FireBaseApiClient.resetPassword(email);
    }

    @Override
    public void updateUserProfile(String userId, String newUsername, String newEmail) throws IOException {
        FireBaseApiClient.updateUserProfile(userId, newUsername, newEmail);
    }
}
