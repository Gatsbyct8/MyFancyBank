import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.List;


enum CollateralType{ESTATE, VEHICLE, DEVICE}

public class LoanInterface extends JFrame
{
    private CustomerHomePage father;
    private List<Account> accounts;
    private Customer customer;
    private CollateralType collateral;
    private MoneyAccount targetAccount;
    private MoneyAccount repayAccount;

    private JTextField amountField = new JTextField(4);
    private JComboBox collateralList = new JComboBox<String>();
    private JComboBox accountList = new JComboBox<String>();
    private tableModel loanTableModel = new tableModel();
    private JComboBox repayAccountList = new JComboBox<String>();
    private JTable loanTable = new JTable();
    private JButton jbtRepay = new JButton("Repay");
    LoanInterface(CustomerHomePage father, List<Account> accounts, Customer customer)
    {
        this.father = father;
        this.accounts = accounts;
        this.customer = customer;

        setTitle("Loan");
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        JLabel amountLabel = new JLabel("Amount");
        Font f = new Font("", Font.BOLD, 16);
        amountLabel.setFont(f);
        amountField.setFont(f);
        JPanel amountPanel = new JPanel(new FlowLayout());
        amountPanel.add(amountLabel);
        amountPanel.add(amountField);

        JLabel accountLabel = new JLabel("Account");
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
        JPanel accountPanel = new JPanel(new FlowLayout());
        accountPanel.add(accountLabel);
        accountPanel.add(accountList);

        JLabel collateralLabel = new JLabel("Collateral");
        collateralList.setEditable(false);
        collateralList.setEnabled(true);
        collteralListListener collateralListL = new collteralListListener();
        collateralList.addItemListener(collateralListL);
        for(CollateralType item : CollateralType.values())
        {
            collateralList.addItem(item.toString());
        }
        JPanel collateralPanel = new JPanel(new FlowLayout());
        collateralPanel.add(collateralLabel);
        collateralPanel.add(collateralList);

        JButton jbtLoan = new JButton("Loan");
        loanListener okL = new loanListener();
        jbtLoan.addActionListener(okL);

        JTableHeader loanHeader = loanTable.getTableHeader();
        loanHeader.setResizingAllowed(true);
        JScrollPane loanScroll = new JScrollPane(loanTable);
        String[] loanHeaderName = {"Amount", "Date", "Collateral", "ID"};
        loanTableModel.setColumnIdentifiers(loanHeaderName);
        loanTable.setModel(loanTableModel);
        loanTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jbtRepay.setEnabled(false);
        refreshLoanTable();

        JLabel repayAccountLabel = new JLabel("Repay Account");
        repayAccountList.setEditable(false);
        repayAccountList.setEnabled(true);
        repayAccountListListener repayAccountListL = new repayAccountListListener();
        repayAccountList.addItemListener(repayAccountListL);
        for(Account item : accounts)    // add accounts into the accountList
        {

            if(item instanceof SavingAccount)
            {
                SavingAccount savingAccount = (SavingAccount)item;
                repayAccountList.addItem(savingAccount.toString());
            }
            else if (item instanceof CheckingAccount)
            {
                CheckingAccount checkingAccount = (CheckingAccount)item;
                repayAccountList.addItem(checkingAccount.toString());
            }
        }
        JPanel repayAccountPanel = new JPanel(new FlowLayout());
        repayAccountPanel.add(repayAccountLabel);
        repayAccountPanel.add(repayAccountList);

        repayListener repayL = new repayListener();
        jbtRepay.addActionListener(repayL);

        Dimension buttonSize = new Dimension(100,30);
        Font buttonF = new Font("", Font.BOLD, 15);
        jbtLoan.setPreferredSize(buttonSize);
        jbtLoan.setFont(buttonF);
        jbtRepay.setPreferredSize(buttonSize);
        jbtRepay.setFont(buttonF);

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel(gridBag);

        c.insets = new Insets(10,5,10,5);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(amountPanel, c);
        c = new GridBagConstraints();
        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(accountPanel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(collateralPanel, c);
        gridBag.addLayoutComponent(jbtLoan, c);
        gridBag.addLayoutComponent(loanScroll, c);
        gridBag.addLayoutComponent(repayAccountPanel, c);
        gridBag.addLayoutComponent(jbtRepay, c);

        panel.add(amountPanel);
        panel.add(accountPanel);
        panel.add(collateralPanel);
        panel.add(jbtLoan);
        panel.add(loanScroll);
        panel.add(repayAccountPanel);
        panel.add(jbtRepay);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    class collteralListListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {
                String collteralString = e.getItem().toString();
                for(CollateralType item : CollateralType.values())
                {
                    if(item.toString().equals(collteralString))
                    {
                        collateral = item;
                        break;
                    }
                }
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
                String accountID = selectAccount.substring(selectAccount.length()-4);
                for(Account item : accounts)
                {
                    if(item.getAccountID().equals(accountID) && (item instanceof MoneyAccount))
                    {
                        targetAccount = (MoneyAccount) item;
                        break;
                    }
                }
            }
        }
    }

    class repayAccountListListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {
                String selectAccount = e.getItem().toString();
                String accountID = selectAccount.substring(selectAccount.length()-4);
                for(Account item : accounts)
                {
                    if(item.getAccountID().equals(accountID) && (item instanceof MoneyAccount))
                    {
                        repayAccount = (MoneyAccount) item;
                        break;
                    }
                }
            }
        }
    }

    class loanListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(!isValid(amountField.getText()))
            {
                MessageDialog amountError = new MessageDialog("Error", "Invalid amount!");
                amountError.setVisible(true);
                return;
            }
            double amount = Double.valueOf(amountField.getText());
            Loan newLoan = new Loan(new Date(System.currentTimeMillis()), amount, collateral, Loan.getMAXID());
            Loan.maxIDIncrease();
            customer.addLoan(newLoan);

            Transaction loanTransaction = new Transaction(new Date(System.currentTimeMillis()), amount, "Loan");
            Balance targetBalance = null;
            for(Balance item : targetAccount.getBalance())
            {
                if(item.getCurrencyType().equals("USD"))
                {
                    targetBalance = item;
                    break;
                }
            }
            targetBalance.addNewTransaction(loanTransaction);
            refreshLoanTable();
        }
    }

    class repayListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int index = loanTable.getSelectedRow();
            if(index == -1)
            {
                MessageDialog amountError = new MessageDialog("Error", "Please select a loan!");
                amountError.setVisible(true);
                return;
            }
            String loanID = (String)loanTable.getValueAt(index, 3);

            Balance repayBalance = null;
            for(Balance item : repayAccount.getBalance())
            {
                if(item.getCurrencyType().equals("USD"))
                {
                    repayBalance = item;
                    break;
                }
            }

            if(!customer.repayLoan(loanID, repayBalance, repayAccount))
            {
                MessageDialog amountError = new MessageDialog("Error", "These Account does not have enough money!");
                amountError.setVisible(true);
                return;
            }
            refreshLoanTable();
        }
    }

    class tableModel extends DefaultTableModel
    {
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return false;
        }
    }

    public void refreshLoanTable()
    {
        loanTableModel.setRowCount(0);
        for(Loan item : customer.getLoans())
        {
            String amount = String.format(" %.2f",item.getAmount());
            String date = item.getDate().toString();
            String collateral = item.getCollateral().toString();
            String id = item.getID();
            loanTableModel.addRow(new String[]{amount, date, collateral, id});
        }

        if(loanTableModel.getRowCount() != 0)
        {
            jbtRepay.setEnabled(true);
        }
        //balanceAmount.setText(currentBalance.getCurrencyType() + String.format(" %.2f",currentBalance.getAmount()));
    }

    private boolean isValid(String str)
    {
        String reg = "^[0-9]+(.[0-9]+)?$";
        if(str.matches(reg))
        {
            return Double.valueOf(str) > 0;
        }
        return false;
    }

    private void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }
}
