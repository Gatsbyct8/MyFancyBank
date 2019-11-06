import java.util.List;

public class SecureAccount extends Account
{
    List<SelfStock> selfStock;
    public boolean buy()
    {
        return false;
    }
    public boolean sell()
    {
        return false;
    }
    public boolean openAccount()
    {
        return false;
    }

}
