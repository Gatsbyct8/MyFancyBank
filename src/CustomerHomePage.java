import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * GUI of customer
 */
public class CustomerHomePage extends JFrame
{
    private Customer customer;
    private Bank bank;
    private CustomerHomePage self = this;
    private LoginInterface father;

    CustomerHomePage(Bank bank, Customer customer, LoginInterface father)
    {
        this.bank = bank;
        this.customer = customer;
        this.father = father;

        setTitle("Customer");
        setSize(300,250);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        JPanel panel = new JPanel(gridBag);

        Dimension buttonSize = new Dimension(150,25);
        JButton jbtAccounts = new JButton("Accounts");
        jbtAccounts.setPreferredSize(buttonSize);
        accountsListener accountL = new accountsListener();
        jbtAccounts.addActionListener(accountL);
        JButton jbtLoan = new JButton("Loan");
        jbtLoan.setPreferredSize(buttonSize);
        loanListener loanL = new loanListener();
        jbtLoan.addActionListener(loanL);
        JButton jbtStocks = new JButton("Stocks");
        jbtStocks.setPreferredSize(buttonSize);
        stocksListener stocksL = new stocksListener();
        jbtStocks.addActionListener(stocksL);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(jbtAccounts, c);
        gridBag.addLayoutComponent(jbtLoan, c);
        gridBag.addLayoutComponent(jbtStocks, c);

        panel.add(jbtAccounts);
        panel.add(jbtLoan);
        panel.add(jbtStocks);

        Font f = new Font("", Font.BOLD, 16);
        jbtAccounts.setFont(f);
        jbtLoan.setFont(f);
        jbtStocks.setFont(f);

        setContentPane(panel);
        //pack();
        setResizable(false);
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

    class stocksListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            CustomerStockInterface customerStockInterface = new CustomerStockInterface(customer, bank, self);
            setVisible(false);
            customerStockInterface.setVisible(true);
        }
    }

    private void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }
}
