package app.service;

import app.dto.CreateAccountRequest;
import app.dto.WithdrawAmountRequest;
import app.entity.Account;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    private static String ACCOUNT_EMAIL = "run_test@gmail.com";
    // CVC is 123
    private static String ACCOUNT_CVC = "$2a$10$o5lpFMECBuFytz6wjUGS2OF5J1q7Hajjm.z288Xrn8X0DkgYwZV3O";
    private static final BigDecimal INITIAL_ACCOUNT_BALANCE = new BigDecimal(100000000);

    @BeforeAll
    @Transactional
    public static void init(@Autowired AccountService accountService) {
        CreateAccountRequest testAccount = new CreateAccountRequest();
        testAccount.setEmail(ACCOUNT_EMAIL);
        testAccount.setCvc(ACCOUNT_CVC);
        testAccount.setBalance(INITIAL_ACCOUNT_BALANCE);
        accountService.deleteAccountByEmail(ACCOUNT_EMAIL);
        accountService.createAccount(testAccount);
    }

    @Test
    void testWithdraw_balanceConsistencyInRaceCondition() throws InterruptedException {
        float amount1 = 1000;
        int numberOfWithdrawals1 = 100;
        Thread client1 = new Thread(() -> {
            for (int i = 0; i < numberOfWithdrawals1; i++) {
                accountService.withdraw(new WithdrawAmountRequest(
                        ACCOUNT_EMAIL, new BigDecimal(amount1), ACCOUNT_CVC));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {

                }
            }
        });

        float amount2 = 50;
        int numberOfWithdrawals2 = 500;
        Thread client2 = new Thread(() -> {
            for (int i = 0; i < numberOfWithdrawals2; i++) {
                accountService.withdraw(new WithdrawAmountRequest(
                        ACCOUNT_EMAIL, new BigDecimal(amount2), ACCOUNT_CVC));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {

                }
            }
        });

        client1.start();
        client2.start();

        client1.join();
        client2.join();

        BigDecimal res = accountService.getBalanceByEmail(ACCOUNT_EMAIL).getBalance();
        BigDecimal expected = INITIAL_ACCOUNT_BALANCE.subtract(
                new BigDecimal(
                        amount1 * numberOfWithdrawals1 +
                                amount2 * numberOfWithdrawals2));

        Assertions.assertEquals(expected, res);
        //Actual   :99970250
        //Expected :99875000
    }
}