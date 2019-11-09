import java.sql.Date;

public class Transaction
{
    private Date date;
    private double amount;
    private String sourceNtargetID;
    private String reason;

    Transaction(Date date, double amount, String sourceNtargetID)
    {
        this.date = date;
        this.amount = amount;
        this.sourceNtargetID = sourceNtargetID;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public double getAmount()
    {
        return amount;
    }

    public Date getDate()
    {
        return date;
    }

    public String getSourceNtargetID()
    {
        return sourceNtargetID;
    }
}
