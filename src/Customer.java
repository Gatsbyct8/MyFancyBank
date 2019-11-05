import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer extends Person
{
    private String UserID;
    private String passcode;
    private List<Account> accounts;
    private List<Loan> loans;

    Customer(Name name, Address address, String phoneNumber, String UserID, String passcode)
    {
        super(name, address, phoneNumber);
        this.UserID = UserID;
        this.passcode = passcode;
        accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public void initForTest()
    {
        SavingAccount newSavingAccount = new SavingAccount();
        newSavingAccount.initForTest();
        accounts.add(newSavingAccount);

        CheckingAccount newCheckingAccount = new CheckingAccount();
        newCheckingAccount.initForTest();
        accounts.add(newCheckingAccount);
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

    public List<Account> getAccounts()
    {
        return accounts;
    }

    public void getTotalBalance(){}

    public void addLoan(Loan newLoan)
    {
        loans.add(newLoan);
    }

    public boolean repayLoan(String loanID, Balance balance, Account account)
    {
        Loan repayLoan = null;
        for(Loan item : loans)
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
        int loanTime = seconds/5 + 1;   // Once the customer loan money, there will be interest

        if(account instanceof SavingAccount)
        {
            account.calculateCurrentInterest(balance, Bank.getSavingAccountDepositInterestRate());
        }
        if(account instanceof CheckingAccount)
        {
            account.calculateCurrentInterest(balance, Bank.getCheckingAccountDepositInterestRate());
        }

        double interest = repayLoan.getAmount() * loanTime * (Bank.getLoanInterest() * 0.01);
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

            Transaction loanFee = new Transaction(new Date(), interest, account.getAccountID());
            loanFee.setReason("Loan interest");
            Bank.addIncome(loanFee);

            loans.remove(repayLoan);
            return true;
        }
    }
}
