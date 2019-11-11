import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * customer of bank
 */
public class Customer extends Person
{
    private String UserID;
    private String passcode;
    private List<Account> Accounts;
    private List<Loan> loans;   // loans of this customer

    Customer(Name name, Address address, String phoneNumber, String UserID, String passcode)
    {
        super(name, address, phoneNumber);
        this.UserID = UserID;
        this.passcode = passcode;
        Accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public void initForTest()
    {
        SavingAccount newSavingAccount = new SavingAccount();
        newSavingAccount.setAccountID(MoneyAccount.getMaxID());
        MoneyAccount.maxIDIncrease();
        newSavingAccount.initForTest();
        Accounts.add(newSavingAccount);
        Transaction newTransaction = new Transaction(new Date(), 5, newSavingAccount.getAccountID());
        newTransaction.setReason("Open account fee");
        Bank.addIncome(newTransaction);

        CheckingAccount newCheckingAccount = new CheckingAccount();
        newCheckingAccount.setAccountID(MoneyAccount.getMaxID());
        MoneyAccount.maxIDIncrease();
        newCheckingAccount.initForTest();
        Accounts.add(newCheckingAccount);
        newTransaction = new Transaction(new Date(), 5, newCheckingAccount.getAccountID());
        newTransaction.setReason("Open account fee");
        Bank.addIncome(newTransaction);
    }
    
    public boolean openSecureAccount(SavingAccount sa) {
    	//Need a standard to decide if it is OK to open a secureAccount
    	//default: US Dollar, based on the USD currency of the balance of the money account
    	
    	this.Accounts.add(new SecureAccount());
    	
    	return true;
    }
    
    public List<SecureAccount> getSecureAccounts(){
    	List<SecureAccount> sas = new ArrayList<SecureAccount>();
    	for(Iterator<Account> iterator = Accounts.iterator();iterator.hasNext();) {
			Account one = iterator.next();
			if(one instanceof SecureAccount) {
				sas.add((SecureAccount)one);
			}
		}
    	return sas;
    }
    
    public Account getAccountById(String id) {
    	for(Iterator<Account> iterator = Accounts.iterator();iterator.hasNext();) {
			Account one = iterator.next();
			if(one.accountID.matches(id)) {
				return one;
			}
		}
    	return null;
    }

    public Name getName()
    {
        return name;
    }

    public List<Loan> getLoans()
    {
        return loans;
    }

    public String getUserID()
    {
        return UserID;
    }

    public String getPasscode()
    {
        return passcode;
    }

    public List<Account> getAccounts()
    {
        return Accounts;
    }

    public void addLoan(Loan newLoan)
    {
        loans.add(newLoan);
    }

    //repay the loan
    public boolean repayLoan(String loanID, Balance balance, MoneyAccount moneyAccount)
    {
        Loan repayLoan = null;
        for(Loan item : loans)  // find the loan
        {
            if(item.getID().equals(loanID))
            {
                repayLoan = item;
                break;
            }
        }

        long startTime = repayLoan.getDate().getTime();
        long endTime = new Date().getTime();
        int seconds = (int)((endTime - startTime) / 1000);
        int loanTime = seconds/5 + 1;   // Once the customer loan money, there will be interest, slightly different with deposit interest, for the bank wants to make more money

        // calculate the interest until now of accounts, for they maybe ca repay the loan with the interest
        if(moneyAccount instanceof SavingAccount)
        {
            moneyAccount.calculateCurrentInterest(balance, Bank.getSavingAccountDepositInterestRate());
        }
        if(moneyAccount instanceof CheckingAccount)
        {
            moneyAccount.calculateCurrentInterest(balance, Bank.getCheckingAccountDepositInterestRate());
        }

        double interest = repayLoan.getAmount() * loanTime * (Bank.getLoanInterest() * 0.01);   // calculate the loan interest
        if((repayLoan.getAmount() + interest) > balance.getAmount())
        {   // the account doesn't have enough money
            return false;
        }
        else
        {
            Transaction repayTransaction = new Transaction(new Date(), -repayLoan.getAmount(), "Loan repay");
            balance.addNewTransaction(repayTransaction);
            Transaction repayFeeTransaction = new Transaction(new Date(), -interest, "Loan interest");
            balance.addNewTransaction(repayFeeTransaction);

            // profit of this loan
            Transaction loanFee = new Transaction(new Date(), interest, moneyAccount.getAccountID());
            loanFee.setReason("Loan interest");
            Bank.addIncome(loanFee);

            loans.remove(repayLoan);
            return true;
        }
    }
}
