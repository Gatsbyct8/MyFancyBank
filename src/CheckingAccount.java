import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CheckingAccount extends Account
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
        Balance newBalance = new Balance();
        newBalance.initForTest();
        balance.add(newBalance);
        accountID = "0000000000000002";
    }

    public boolean withdraw(Balance balance, double interestRate, double amount, double withdrawFee)
    {
        calculateCurrentInterest(balance, interestRate);
        double balanceAmount = balance.getAmount();
        if((amount + withdrawFee) <= balanceAmount)
        {
            Transaction newWithdraw = new Transaction(new Date(), -amount, "Withdraw");
            balance.addNewTransaction(newWithdraw);
            Transaction newWithdrawFee = new Transaction(new Date(), -withdrawFee, "Withdraw fee");
            balance.addNewTransaction(newWithdrawFee);

            Transaction fee = new Transaction(new Date(), withdrawFee, accountID);
            fee.setReason("Withdraw fee");
            Bank.addIncome(fee);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean transferOut(Balance balance, double amount, Account targetAccount)
    {
        calculateCurrentInterest(balance, Bank.getCheckingAccountDepositInterestRate());
        if(amount <= balance.getAmount())
        {
            Transaction newTransaction = new Transaction(new Date(), -amount, targetAccount.getAccountID());
            balance.addNewTransaction(newTransaction);
            return true;
        }
        else
        {
            return false;
        }
    }
}
