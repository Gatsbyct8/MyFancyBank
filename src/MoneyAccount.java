import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class MoneyAccount extends Account
{

    protected List<Balance> balance;

    MoneyAccount()
    {
        balance = new ArrayList<>();
    }

    public List<Balance> getBalance()
    {
        return balance;
    }

    public void addNewBalance(Balance newBalance)
    {
        balance.add(newBalance);
    }
    
    public Balance getUSDBalance() {
    	for(Iterator<Balance> iterator = balance.iterator();iterator.hasNext();) {
			Balance one = iterator.next();
			if(one.getCurrency().equals(CurrencyType.getCurrencyType(CurrencyType.USD))) {
				return one;
			}
		}
    	System.out.println("There is no USD balance");
    	return null;
    }

    public void calculateCurrentInterest(Balance balance, double interestRate)
    {
        List<Transaction> transactions = balance.getTransactions();
        Date lastTransactionDate = null;
        Date now = new Date();
        if(transactions.size() > 0)
        {
            Transaction lastTransaction = transactions.get(transactions.size() - 1);
             lastTransactionDate = lastTransaction.getDate();
        }
        else
        {
            lastTransactionDate = now;
        }

        long startTime = lastTransactionDate.getTime();
        long endTime = now.getTime();
        int seconds = (int)((endTime - startTime) / 1000);
        int depositTime = seconds/5;
        double interest = interestRate * depositTime; // For test convinence, I set the interest rate to be 5 seconds interest rate, but not day interest rate
        if(interest == 0)
        {
            return;
        }
        Transaction interestTransaction = new Transaction(new Date(), interest, "Interest");
        balance.addNewTransaction(interestTransaction);
    }

    public boolean withdraw(Balance balance, double interestRate, double amount)
    {
        calculateCurrentInterest(balance, interestRate);
        double balanceAmount = balance.getAmount();
        if((amount) <= balanceAmount)
        {
            Transaction newWithdraw = new Transaction(new Date(), -amount, "Withdraw");
            balance.addNewTransaction(newWithdraw);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void deposit(Balance balance, double interestRate, double amount)
    {
        calculateCurrentInterest(balance, interestRate);
        Transaction newDeposit = new Transaction(new Date(), amount, "Deposit");
        balance.addNewTransaction(newDeposit);
    }

    public void transferIn(Balance balance, double interestRate, double amount, MoneyAccount targetMoneyAccount)
    {
        calculateCurrentInterest(balance, interestRate);
        double transactionFee = amount * Bank.getTransactionFeeRate() * 0.01;
        Transaction newTransaction = new Transaction(new Date(), amount, targetMoneyAccount.getAccountID());
        balance.addNewTransaction(newTransaction);
        Transaction newTransactionFee = new Transaction(new Date(), -transactionFee, "Transaction fee");
        balance.addNewTransaction(newTransactionFee);

        Transaction fee = new Transaction(new Date(), transactionFee, accountID);
        fee.setReason("Transaction fee");
        Bank.addIncome(fee);
    }
}
