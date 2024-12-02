package infrastructure.interfaces;

import domain.entities.PortfolioEntry;
import domain.entities.Transaction;

import java.io.IOException;
import java.util.List;

/**
 * Interface for user management and financial operations.
 */
public interface UserApi {
    String loginUser(String email, String password) throws IOException;

    String registerUser(String username, String email, String password, String securityQuestion, String securityAnswer) throws IOException;

    void addCash(String userId, double amount) throws IOException;

    void withdrawCash(String userId, double amount) throws IOException;

    double getCashReserves(String userId) throws IOException;

    List<Transaction> getTransactions(String userId) throws IOException;

    List<PortfolioEntry> getPortfolioEntries(String userId) throws IOException;

    void addTransactionAndUpdatePortfolio(String userId, Transaction transaction) throws IOException;

    void resetPassword(String email) throws IOException;

    void updateUserProfile(String userId, String newUsername, String newEmail) throws IOException;
}
