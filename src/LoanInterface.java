import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
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

        accountList.setEditable(false);
        accountList.setEnabled(true);
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

        collateralList.setEditable(false);
        collateralList.setEnabled(true);
        collteralListListener collteralListL = new collteralListListener();
        collateralList.addItemListener(collteralListL);
        for(CollateralType item : CollateralType.values())
        {
            collateralList.addItem(item.toString());
        }

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

        repayListener repayL = new repayListener();
        jbtRepay.addActionListener(repayL);

        add(amountField);
        add(accountList);
        add(collateralList);
        add(jbtLoan);
        add(loanScroll);
        add(repayAccountList);
        add(jbtRepay);
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
            Loan newLoan = new Loan(new Date(), amount, collateral, Loan.getMAXID());
            Loan.maxIDIncrease();
            customer.addLoan(newLoan);

            Transaction loanTransaction = new Transaction(new Date(), amount, "Loan");
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
