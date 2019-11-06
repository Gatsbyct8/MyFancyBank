import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class CustomerStockInterface extends JFrame
{
    private Customer customer;
    private Bank bank;
    private CustomerStockInterface self = this;
    private CustomerHomePage father;
    private JComboBox<SecureAccount> secureAccountJComboBox = new JComboBox<SecureAccount>();
    private JComboBox<SavingAccount> savingAccountJComboBox = new JComboBox<SavingAccount>();
    private JComboBox<Balance> balanceJComboBox = new JComboBox<Balance>();
    private JTable stockTable = new JTable();

    CustomerStockInterface(Customer customer, Bank bank, CustomerHomePage father)
    {
        this.customer = customer;
        this.bank = bank;
        this.father = father;

        setTitle("Stocks");
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        secureAccountJComboBox.setEditable(false);
        secureAccountJComboBox.setEnabled(true);
        secureAccountJComboBoxListener secureAccountJComboBoxL = new secureAccountJComboBoxListener();
        secureAccountJComboBox.addItemListener(secureAccountJComboBoxL);
        List<Account> Accounts = customer.getAccounts();
        for(Account item : Accounts)
        {

        }

    }

    class secureAccountJComboBoxListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {

            }
        }
    }


    private void closeFrame()
    {
        dispose();
        father.setVisible(true);
        toFront();
    }
}
