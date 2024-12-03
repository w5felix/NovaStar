package application.interactors.withdraw_cash;

import domain.entities.User;
import infrastructure.interfaces.UserApi;

import java.io.IOException;

public class WithdrawCashInteractor {
    private final UserApi userApi;

    public WithdrawCashInteractor(UserApi userApi) {
        this.userApi = userApi;
    }

    public void execute(User user, double amount) throws IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (user.getCashBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        userApi.withdrawCash(user.getUserId(), amount);
        user.setCashBalance(user.getCashBalance() - amount);
    }
}
