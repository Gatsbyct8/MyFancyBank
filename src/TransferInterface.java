import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TransferInterface extends JFrame
{
    private Bank bank;
    private Account account;
    private Balance balance;
    private AccountInterface father;

    private JTextField amountField=new JTextField(4);
    private JTextField idField=new JTextField(4);

    TransferInterface(AccountInterface father, Bank bank, Account account, Balance balance)
    {
        this.father = father;
        this.bank = bank;
        this.account = account;
        this.balance = balance;

        setTitle("Transfer");
        setSize(500,200);
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

        JLabel amountLabel = new JLabel("Amount");
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new FlowLayout());
        amountPanel.add(amountLabel);
        amountPanel.add(amountField);

        JLabel idLabel = new JLabel("Target ID");
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout());
        idPanel.add(idLabel);
        idPanel.add(idField);

        JButton jbtOK = new JButton("OK");
        okListener okL = new okListener();
        jbtOK.addActionListener(okL);

        add(amountPanel);
        add(idPanel);
        add(jbtOK);
    }

    class okListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(isValid(amountField.getText()) && isValid(idField.getText()))
            {
                Account targetAccount = findAccount(idField.getText());
                if(targetAccount == null)
                {
                    MessageDialog amountError = new MessageDialog("Error", "Wrong account id!");
                    amountError.setVisible(true);
                    return;
                }
                double transferAmount = Double.valueOf(amountField.getText());
                if(!((CheckingAccount)account).transferOut(balance, transferAmount, targetAccount))
                {
                    MessageDialog amountError = new MessageDialog("Error", "No enough money!");
                    amountError.setVisible(true);
                    return;
                }
                double interestRate = 0;
                if(targetAccount instanceof SavingAccount)
                {
                    interestRate = Bank.getSavingAccountDepositInterestRate();
                }
                if(targetAccount instanceof CheckingAccount)
                {
                    interestRate = Bank.getCheckingAccountDepositInterestRate();
                }

                Balance targetBalance = null;
                //Search the target balance of target account.
                for(Balance item : targetAccount.getBalance())
                {
                    if(item.getCurrency().equals(balance.getCurrency()))
                    {
                        targetBalance = item;
                        break;
                    }
                }

                //If target account doesn't have such kind of currency balance, create this balance for it
                if(targetBalance == null)
                {
                    Balance newBalance = new Balance(balance.getCurrency());
                    targetAccount.addNewBalance(newBalance);
                    targetBalance = newBalance;
                }
                targetAccount.transferIn(targetBalance, interestRate, transferAmount, account);

                father.initAccountList();
            }
            else
            {
                MessageDialog amountError = new MessageDialog("Error", "Invalid input!");
                amountError.setVisible(true);
                return;
            }

            closeFrame();
        }
    }

    private Account findAccount(String id)
    {
        Account targetAccount = null;
        for(Customer customer : bank.getCustomers())
        {
            for(Account account : customer.getAccounts())
            {
                if(account.getAccountID().equals(id))
                {
                    targetAccount = account;
                    return  targetAccount;
                }
            }
        }

        return targetAccount;
    }

    private boolean isValid(String str)
    {
        String reg = "^[0-9]+(.[0-9]+)?$";
        if(str.matches(reg))
        {
            return Double.valueOf(str) >= 0;
        }
        return false;
    }

    private void closeFrame()
    {
        father.setEnabled(true);
        father.refreshTransactionTable();
        dispose();
    }
}
