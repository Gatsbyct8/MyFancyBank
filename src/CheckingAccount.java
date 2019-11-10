import java.util.Currency;
import java.sql.Date;
import java.util.Locale;


public class CheckingAccount extends MoneyAccount
{
    public static final AccountType type = AccountType.CHECKING;

    CheckingAccount()
    {
        super();
    }

    public String toString()
    {
        return "Checking Account " + accountID;
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

    public boolean withdraw(Balance balance, double interestRate, double amount, double withdrawFee)
    {
        calculateCurrentInterest(balance, interestRate);
        double balanceAmount = balance.getAmount();
        if((amount + withdrawFee) <= balanceAmount)
        {
            Transaction newWithdraw = new Transaction(new Date(System.currentTimeMillis()), -amount, "Withdraw");
            balance.addNewTransaction(newWithdraw);
            Transaction newWithdrawFee = new Transaction(new Date(System.currentTimeMillis()), -withdrawFee, "Withdraw fee");
            balance.addNewTransaction(newWithdrawFee);

            Transaction fee = new Transaction(new Date(System.currentTimeMillis()), withdrawFee, accountID);
            fee.setReason("Withdraw fee");
            Bank.addIncome(fee);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean transferOut(Balance balance, double amount, MoneyAccount targetAccount)
    {
        calculateCurrentInterest(balance, Bank.getCheckingAccountDepositInterestRate());
        if(amount <= balance.getAmount())
        {
            Transaction newTransaction = new Transaction(new Date(System.currentTimeMillis()), -amount, targetAccount.getAccountID());
            balance.addNewTransaction(newTransaction);
            return true;
        }
        else
        {
            return false;
        }
    }
}
