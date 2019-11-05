

public class ATM
{

    private Bank bank;

    ATM(Bank bank)
    {
        this.bank = bank;
    }

    public void Run()
    {
        HomePage homepage = new HomePage(this.bank);
        homepage.setVisible(true);
    }
}
