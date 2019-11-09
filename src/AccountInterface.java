import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AccountInterface extends JFrame
{
    Bank bank;
    private List<Account> accounts;
    private MoneyAccount currentMoneyAccount = null;
    private Balance currentBalance = null;
    private CustomerHomePage father;
    private AccountInterface self = this;

    private JComboBox accountList = new JComboBox<String>();
    private JComboBox balanceList = new JComboBox<String>();
    private JLabel balanceAmount = new JLabel();
    private DefaultTableModel transactionTableModel = new DefaultTableModel();
    private JTable transactionTable = new JTable();
    private JButton jbtTransfer = new JButton("Transfer");

    AccountInterface(Bank bank, List<Account> accounts, CustomerHomePage father)
    {
        this.bank = bank;
        this.accounts = accounts;
        this.father = father;

        setTitle("Accounts");
        setSize(500,700);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        //Dimension JComboBoxSize = new Dimension(100,20);
        accountList.setEditable(false);
        accountList.setEnabled(true);
        accountList.setPreferredSize(new Dimension(175,20));
        accountListListener accountListL = new accountListListener();
        accountList.addItemListener(accountListL);
        for(Account item : accounts)    // add accounts into the accountList
        {

            if(item instanceof SavingAccount)
            {
                SavingAccount savingAccount = (SavingAccount)item;
                accountList.addItem(savingAccount.toString());
            }
            else if (item instanceof CheckingAccount)
            {
                CheckingAccount checkingAccount = (CheckingAccount)item;
                accountList.addItem(checkingAccount.toString());
            }
        }
        if(currentMoneyAccount == null)
        {
            jbtTransfer.setEnabled(false);
        }

        balanceList.setEditable(false);
        balanceList.setEnabled(true);
        balanceList.setPreferredSize(new Dimension(50,20));
        balanceListListener balanceListL = new balanceListListener();
        balanceList.addItemListener(balanceListL);

        balanceAmount.setText("0.00");

        JTableHeader transactionHeader = transactionTable.getTableHeader();
        transactionHeader.setResizingAllowed(true);
        JScrollPane transactionScroll = new JScrollPane(transactionTable);
        String[] transactionHeaderName = {"Amount", "Date", "Source\\Target"};
        transactionTableModel.setColumnIdentifiers(transactionHeaderName);
        transactionTable.setModel(transactionTableModel);
        transactionTable.setEnabled(false);
        refreshTransactionTable();

        JButton jbtDiposit = new JButton("Deposit");
        depositListener depositL = new depositListener();
        jbtDiposit.addActionListener(depositL);

        JButton jbtWithdraw = new JButton("Withdraw");
        withdrawListener withdrawL = new withdrawListener();
        jbtWithdraw.addActionListener(withdrawL);


        transferListener transferL = new transferListener();
        jbtTransfer.addActionListener(transferL);

        JButton jbtopenAccount = new JButton("Open New Account");
        openNewAccountListener openNewAccountL = new openNewAccountListener();
        jbtopenAccount.addActionListener(openNewAccountL);

        add(accountList);
        add(balanceList);
        add(balanceAmount);
        add(transactionScroll);
        add(jbtDiposit);
        add(jbtWithdraw);
        add(jbtTransfer);
        add(jbtopenAccount);
    }

    public void initAccountList()
    {
        accountList.removeAllItems();
        for(Account item : accounts)    // add accounts into the accountList
        {

            if(item instanceof SavingAccount)
            {
                SavingAccount savingAccount = (SavingAccount)item;
                accountList.addItem(savingAccount.toString());
            }else if (item instanceof CheckingAccount)
            {
                CheckingAccount checkingAccount = (CheckingAccount)item;
                accountList.addItem(checkingAccount.toString());
            }
        }
    }

    class accountListListener implements ItemListener
    {

        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {
                String selectAccount = e.getItem().toString();
                String accountID = selectAccount.substring(selectAccount.length()-4, selectAccount.length());
                for(Account item : accounts)
                {
                    if(item.getAccountID().equals(accountID) && (item instanceof MoneyAccount))
                    {
                        currentMoneyAccount = (MoneyAccount) item;
                        break;
                    }
                }

                balanceList.removeAllItems();
                for(Balance item : currentMoneyAccount.getBalance())
                {
                    balanceList.addItem(item.getCurrencyType());
                }
                for(Balance item : currentMoneyAccount.getBalance())
                {
                    if(item.getCurrencyType().equals(balanceList.getItemAt(0)))
                    {
                        currentBalance = item;
                        break;
                    }
                }

                if((currentMoneyAccount instanceof SavingAccount) || (currentMoneyAccount == null))
                {
                    jbtTransfer.setEnabled(false);
                }
                if(currentMoneyAccount instanceof CheckingAccount)
                {
                    jbtTransfer.setEnabled(true);
                }
            }
        }
    }

    class balanceListListener implements ItemListener
    {

        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {
                String selectBalance = e.getItem().toString();
                if(selectBalance.equals("Select an currency..."))
                {
                    return;
                }
                for(Balance item : currentMoneyAccount.getBalance())
                {
                    if(item.getCurrencyType().equals(selectBalance))
                    {
                        currentBalance = item;
                        break;
                    }
                }
                balanceAmount.setText(currentBalance.getCurrencyType() + String.format(" %.2f",currentBalance.getAmount()));

                refreshTransactionTable();
            }
        }
    }

    class depositListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(currentMoneyAccount == null)
            {
                MessageDialog errorMessage = new MessageDialog("Error", "Please select an account!");
                errorMessage.setVisible(true);
                return;
            }
            DepositInterface depositInterface = new DepositInterface(self, currentMoneyAccount, currentBalance);
            setEnabled(false);
            depositInterface.setVisible(true);

        }
    }

    class withdrawListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(currentMoneyAccount == null)
            {
                MessageDialog errorMessage = new MessageDialog("Error", "Please select an account!");
                errorMessage.setVisible(true);
                return;
            }
            WithdrawInterface withdrawInterface = new WithdrawInterface(self, currentMoneyAccount, currentBalance);
            setEnabled(false);
            withdrawInterface.setVisible(true);
        }
    }

    class transferListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            TransferInterface transferInterface = new TransferInterface(self, bank, currentMoneyAccount, currentBalance);
            setEnabled(false);
            transferInterface.setVisible(true);
        }
    }

    class openNewAccountListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            OpenNewAccount openNewAccount = new OpenNewAccount(accounts, self);
            setEnabled(false);
            openNewAccount.setVisible(true);
        }
    }

    public void refreshTransactionTable()
    {
        if(currentBalance == null)
        {
            return;
        }
        transactionTableModel.setRowCount(0);
        for(Transaction item : currentBalance.getTransactions())
        {
            String amount = String.format(" %.2f",item.getAmount());
            String date = item.getDate().toString();
            String ID = item.getSourceNtargetID();
            transactionTableModel.addRow(new String[]{amount, date, ID});
        }

        balanceAmount.setText(currentBalance.getCurrencyType() + String.format(" %.2f",currentBalance.getAmount()));
    }

    private void closeFrame()
    {
        dispose();
        father.setVisible(true);
        toFront();
    }
}
