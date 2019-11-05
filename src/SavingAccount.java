import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavingAccount extends Account
{
    public static final AccountType type = AccountType.SAVING;

    SavingAccount()
    {
        super();
    }

    public String toString()
    {
        return "Saving Account " + accountID;
    }

    public void initForTest()
    {
        Balance newBalance = new Balance();
        newBalance.initForTest();
        balance.add(newBalance);

        accountID = "0000000000000001";
    }

    /*public void moneyTransfer(Transaction transaction, Balance balance)
    {
        //calculateCurrentInterest(balance, Bank.getSavingAccountDepositInterestRate());
        balance.addNewTransaction(transaction);
    }*/
}
