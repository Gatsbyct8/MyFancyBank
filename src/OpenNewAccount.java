import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;

public class OpenNewAccount extends JDialog
{
    private List<Account> accounts;
    private AccountType newAccountType;
    private AccountInterface father;

    JComboBox accountType = new JComboBox<String>();
    private JCheckBox USDBox = new JCheckBox(CurrencyType.USD.toString(),true);
    private JCheckBox EURBox = new JCheckBox(CurrencyType.EUR.toString(),true);
    private JCheckBox CNYBox = new JCheckBox(CurrencyType.CNY.toString(),true);
    private JCheckBox JPYBox = new JCheckBox(CurrencyType.JPY.toString());
    OpenNewAccount(List<Account> accounts, AccountInterface father)
    {
        this.father = father;
        this.accounts = accounts;
        setTitle("Open Account");
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        accountType.setPreferredSize(new Dimension(100,20));
        accountType.setEditable(false);
        accountType.setEnabled(true);
        for(AccountType item : AccountType.values())
        {
            accountType.addItem(item.toString());
            //newAccountType = item;
        }
        newAccountType = AccountType.valueOf((String)accountType.getItemAt(0));
        accountTypeListener accountTypeL = new accountTypeListener();
        accountType.addItemListener(accountTypeL);

        JPanel currencyType = new JPanel();
        currencyType.setLayout(new FlowLayout());
        USDBox.setEnabled(false);
        currencyType.add(USDBox);
        EURBox.setEnabled(false);
        currencyType.add(EURBox);
        CNYBox.setEnabled(false);
        currencyType.add(CNYBox);
        currencyType.add(JPYBox);

        JButton jbtOK = new JButton("OK");
        okListener okL = new okListener();
        jbtOK.addActionListener(okL);

        JButton jbtCancel = new JButton("Cancel");
        cancelListener cancelL = new cancelListener();
        jbtCancel.addActionListener(cancelL);

        Dimension buttonSize = new Dimension(75,20);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        jbtOK.setPreferredSize(buttonSize);
        jbtCancel.setPreferredSize(buttonSize);
        buttonPanel.add(jbtOK);
        buttonPanel.add(jbtCancel);

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel(gridBag);

        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(accountType, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(currencyType, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(buttonPanel, c);

        panel.add(accountType);
        panel.add(currencyType);
        panel.add(buttonPanel);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    class accountTypeListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {
                for(AccountType item : AccountType.values())
                {
                    if(item.toString().equals(e.getItem().toString()))
                    {
                        newAccountType = item;
                        break;
                    }
                }
            }
        }
    }

    class okListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            MoneyAccount newAccount = AccountType.getAccount(newAccountType);
            if(USDBox.isSelected())
            {
                Balance newBalance = new Balance(CurrencyType.getCurrencyType(CurrencyType.USD));
                Transaction firstDeposit = new Transaction(new Date(), Bank.getAccountFee(), "Deposit");
                Transaction openAccountFee = new Transaction(new Date(), -Bank.getAccountFee(), "Open Account Fee");
                newBalance.addNewTransaction(firstDeposit);
                newBalance.addNewTransaction(openAccountFee);
                newAccount.addNewBalance(newBalance);
            }
            if(EURBox.isSelected())
            {
                Balance newBalance = new Balance(CurrencyType.getCurrencyType(CurrencyType.EUR));
                newAccount.addNewBalance(newBalance);
            }
            if(CNYBox.isSelected())
            {
                Balance newBalance = new Balance(CurrencyType.getCurrencyType(CurrencyType.CNY));
                newAccount.addNewBalance(newBalance);
            }
            if(JPYBox.isSelected())
            {
                Balance newBalance = new Balance(CurrencyType.getCurrencyType(CurrencyType.JPY));
                newAccount.addNewBalance(newBalance);
            }
            // If less than three currency type is selected
            if(newAccount.getBalance().size()<3)
            {
                MessageDialog noCurrencyTypeError = new MessageDialog("Error", "Please select at least three currency types! ");
                noCurrencyTypeError.setVisible(true);
                return;
            }

            // Update the maxID of this bank
            MoneyAccount.maxIDIncrease();
            newAccount.setAccountID(MoneyAccount.getMaxID());
            accounts.add(newAccount);

            //add the open account fee as income of the bank
            Transaction accountFee = new Transaction(new Date(), Bank.getAccountFee(), newAccount.getAccountID());
            accountFee.setReason("Open account");
            Bank.addIncome(accountFee);

            father.initAccountList();
            closeFrame();
        }
    }

    class cancelListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            dispose();
        }
    }

    private void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }
}
