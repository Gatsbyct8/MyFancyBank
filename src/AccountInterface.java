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

    private JLabel accountLabel = new JLabel("Account");
    private JComboBox accountList = new JComboBox<String>();
    private JLabel balanceLabel = new JLabel("Balance");
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
        balanceList.setPreferredSize(new Dimension(75,20));
        balanceListListener balanceListL = new balanceListListener();
        balanceList.addItemListener(balanceListL);

        Font f = new Font("", Font.BOLD, 16);
        balanceAmount.setFont(f);
        balanceAmount.setText("0.00");

        JTableHeader transactionHeader = transactionTable.getTableHeader();
        transactionHeader.setResizingAllowed(true);
        JScrollPane transactionScroll = new JScrollPane(transactionTable);
        String[] transactionHeaderName = {"Amount", "Date", "Source\\Target"};
        transactionTableModel.setColumnIdentifiers(transactionHeaderName);
        transactionTable.setModel(transactionTableModel);
        transactionTable.setEnabled(false);
        refreshTransactionTable();

        Dimension buttonSize = new Dimension(100,25);
        JButton jbtDiposit = new JButton("Deposit");
        jbtDiposit.setPreferredSize(buttonSize);
        depositListener depositL = new depositListener();
        jbtDiposit.addActionListener(depositL);
        JButton jbtWithdraw = new JButton("Withdraw");
        jbtWithdraw.setPreferredSize(buttonSize);
        withdrawListener withdrawL = new withdrawListener();
        jbtWithdraw.addActionListener(withdrawL);
        jbtTransfer.setPreferredSize(buttonSize);
        transferListener transferL = new transferListener();
        jbtTransfer.addActionListener(transferL);
        JButton jbtopenAccount = new JButton("Open New Account");
        jbtopenAccount.setPreferredSize(new Dimension(150, 25));
        openNewAccountListener openNewAccountL = new openNewAccountListener();
        jbtopenAccount.addActionListener(openNewAccountL);

        JPanel amountPanel = new JPanel(new FlowLayout());
        amountPanel.add(accountLabel);
        amountPanel.add(accountList);
        JPanel balancePanel = new JPanel(new FlowLayout());
        balancePanel.add(balanceLabel);
        balancePanel.add(balanceList);

        JPanel moneyButtonPanel = new JPanel(new FlowLayout());
        moneyButtonPanel.add(jbtDiposit);
        moneyButtonPanel.add(jbtWithdraw);
        moneyButtonPanel.add(jbtTransfer);

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel(gridBag);

        c.insets = new Insets(10,5,10,5);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.addLayoutComponent(amountPanel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(balancePanel, c);
        gridBag.addLayoutComponent(balanceAmount, c);
        gridBag.addLayoutComponent(transactionScroll, c);
        c = new GridBagConstraints();
        c.insets = new Insets(10,5,10,5);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(moneyButtonPanel, c);
        gridBag.addLayoutComponent(jbtopenAccount, c);

        panel.add(amountPanel);
        panel.add(balancePanel);
        panel.add(balanceAmount);
        panel.add(transactionScroll);
        panel.add(moneyButtonPanel);
        panel.add(jbtopenAccount);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
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
