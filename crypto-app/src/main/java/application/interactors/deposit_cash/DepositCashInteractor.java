package application.interactors.deposit_cash;

import domain.entities.User;
import infrastructure.interfaces.UserApi;

import java.io.IOException;

public class DepositCashInteractor {
    private final UserApi userApi;

    public DepositCashInteractor(UserApi userApi) {
        this.userApi = userApi;
    }

    public void execute(User user, double amount) throws IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        userApi.addCash(user.getUserId(), amount);
        user.setCashBalance(user.getCashBalance() + amount);
    }
}
