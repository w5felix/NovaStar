package adapters;

import api.FireBaseAPIClient;
import entities.PortfolioEntry;
import entities.Transaction;
import interfaces.UserApi;

import java.io.IOException;
import java.util.List;

/**
 * Adapter for Firebase API, implementing UserApi interface.
 */
public class FirebaseServiceAdapter implements UserApi {

    @Override
    public String loginUser(String email, String password) throws IOException {
        return FireBaseAPIClient.loginUser(email, password);
    }

    @Override
    public String registerUser(String username, String email, String password, String securityQuestion, String securityAnswer) throws IOException {
        return FireBaseAPIClient.registerUser(username, email, password, securityQuestion, securityAnswer);
    }

    @Override
    public void addCash(String userId, double amount) throws IOException {
        FireBaseAPIClient.addCash(userId, amount);
    }

    @Override
    public void withdrawCash(String userId, double amount) throws IOException {
        FireBaseAPIClient.withdrawCash(userId, amount);
    }

    @Override
    public double getCashReserves(String userId) throws IOException {
        return FireBaseAPIClient.getCashReserves(userId);
    }

    @Override
    public List<Transaction> getTransactions(String userId) throws IOException {
        return FireBaseAPIClient.getTransactions(userId);
    }

    @Override
    public List<PortfolioEntry> getPortfolioEntries(String userId) throws IOException {
        return FireBaseAPIClient.getPortfolioEntries(userId);
    }

    @Override
    public void addTransactionAndUpdatePortfolio(String userId, Transaction transaction) throws IOException {
        FireBaseAPIClient.addTransactionAndUpdatePortfolio(userId, transaction);
    }

    @Override
    public void resetPassword(String email) throws IOException {
        FireBaseAPIClient.resetPassword(email);
    }

    @Override
    public void updateUserProfile(String userId, String newUsername, String newEmail) throws IOException {
        FireBaseAPIClient.updateUserProfile(userId, newUsername, newEmail);
    }
}
