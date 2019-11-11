import java.util.Currency;
import java.util.Locale;

public class SavingAccount extends MoneyAccount
{
    public static final AccountType type = AccountType.SAVING;

    public SavingAccount()
    {
        super();
    }

    public String toString()
    {
        return "Saving Account " + accountID;
    }

    public void initForTest()
    {
        Balance newBalance = new Balance(Currency.getInstance(Locale.US));
        newBalance.initForTest();
        balance.add(newBalance);
        newBalance = new Balance(Currency.getInstance(Locale.FRANCE));
        balance.add(newBalance);
        newBalance = new Balance(Currency.getInstance(Locale.CHINA));
        balance.add(newBalance);
    }

    /*public void moneyTransfer(Transaction transaction, Balance balance)
    {
        //calculateCurrentInterest(balance, Bank.getSavingAccountDepositInterestRate());
        balance.addNewTransaction(transaction);
    }*/
}
