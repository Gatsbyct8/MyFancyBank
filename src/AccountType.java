public enum AccountType
{
    CHECKING,
    SAVING;

    public static MoneyAccount getAccount(AccountType type)
    {
        switch(type)
        {
            case CHECKING:
                return new CheckingAccount();
            case SAVING:
                return new SavingAccount();
            default:
                return null;
        }
    }
}
