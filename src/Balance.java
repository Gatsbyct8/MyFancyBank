import java.sql.Date;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class Balance
{

    private double amount;
    private Currency currency;
    private List<Transaction> transactions;

    Balance()
    {
        amount = 0;
        currency = Currency.getInstance(Locale.US);
        transactions = new ArrayList<>();
    }

    Balance(Currency currency)
    {
        amount = 0;
        this.currency = currency;
        transactions = new ArrayList<>();
    }

    public Currency getCurrency()
    {
        return currency;
    }
    public void setCurrency(Currency currency){
        this.currency=currency;
    }
    public String getCurrencyType()
    {
        return currency.getCurrencyCode();
    }

    public String getCurrencySymbol()
    {
        return currency.getSymbol();
    }

    public double getAmount()
    {
        return amount;
    }
    public void setAmount(double amount){
        this.amount=amount;
    }

    public void addNewTransaction(Transaction newTransaction)
    {
        transactions.add(newTransaction);
        amount += newTransaction.getAmount();
    }

    public void initForTest()
    {
        amount = 100;

        Transaction newTransaction = new Transaction(new Date(System.currentTimeMillis()), 5, "Deposit");
        transactions.add(newTransaction);
        newTransaction = new Transaction(new Date(System.currentTimeMillis()), -5, "Open account");
        transactions.add(newTransaction);
    }

    public List<Transaction> getTransactions()
    {
        return transactions;
    }
}
