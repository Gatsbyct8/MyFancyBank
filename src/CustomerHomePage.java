import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerHomePage extends JFrame
{
    Customer customer;
    Bank bank;
    CustomerHomePage self = this;
    CustomerHomePage(Bank bank, Customer customer)
    {
        this.bank = bank;
        this.customer = customer;

        setTitle("Customer Homepage");
        setSize(400,150);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton jbtAccounts = new JButton("Accounts");
        accountsListener accountL = new accountsListener();
        jbtAccounts.addActionListener(accountL);
        JButton jbtLoan = new JButton("Loan");
        loanListener loanL = new loanListener();
        jbtLoan.addActionListener(loanL);

        add(jbtAccounts);
        add(jbtLoan);
    }

    class accountsListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            AccountInterface accountInterface = new AccountInterface(bank, customer.getAccounts(), self);
            setVisible(false);
            accountInterface.setVisible(true);
        }
    }

    class loanListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            LoanInterface loanInterface = new LoanInterface(self, customer.getAccounts(), customer);
            setVisible(false);
            loanInterface.setVisible(true);
        }
    }
}
