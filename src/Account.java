public class Account
{
    protected static String maxID = "0001";
    protected String accountID;

    public static void maxIDIncrease()
    {
        Long idNUM = Long.valueOf(maxID);
        idNUM++;
        maxID = String.format("%04d", idNUM);
    }

    public void setAccountID(String accountID)
    {
        this.accountID = accountID;
    }

    public String getAccountID()
    {
        return accountID;
    }

    public static String getMaxID()
    {
        return maxID;
    }

    public static void setMaxID(String maxID)
    {
        MoneyAccount.maxID = maxID;
    }
}
