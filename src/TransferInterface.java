import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * interface of transction
 */
public class TransferInterface extends JFrame
{
    private Bank bank;
    private MoneyAccount moneyAccount;
    private Balance balance;
    private AccountInterface father;

    private JTextField amountField=new JTextField(20);   // transfer amount
    private JTextField idField=new JTextField(20);   // target account

    TransferInterface(AccountInterface father, Bank bank, MoneyAccount moneyAccount, Balance balance)
    {
        this.father = father;
        this.bank = bank;
        this.moneyAccount = moneyAccount;
        this.balance = balance;

        setTitle("Transfer");
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = null;

        JLabel amountLabel = new JLabel("Amount", JLabel.CENTER);

        JLabel idLabel = new JLabel("Target ID", JLabel.CENTER);

        JButton jbtOK = new JButton("OK");
        okListener okL = new okListener();
        jbtOK.addActionListener(okL);

        JPanel panel = new JPanel(gridBag);

        c = new GridBagConstraints();
        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(amountLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(amountField, c);

        c = new GridBagConstraints();
        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(idLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(idField, c);

        gridBag.addLayoutComponent(jbtOK, c);

        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(idLabel);
        panel.add(idField);
        panel.add(jbtOK);

        setContentPane(panel);
        pack();
        setResizable(false);
    }

    class okListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(isValid(amountField.getText()) && isValid(idField.getText()))
            {
                MoneyAccount targetMoneyAccount = findAccount(idField.getText());
                if(targetMoneyAccount == null)
                {
                    MessageDialog amountError = new MessageDialog("Error", "Wrong account id!");
                    amountError.setVisible(true);
                    return;
                }
                double transferAmount = Double.valueOf(amountField.getText());
                if(!((CheckingAccount) moneyAccount).transferOut(balance, transferAmount, targetMoneyAccount))
                {
                    MessageDialog amountError = new MessageDialog("Error", "No enough money!");
                    amountError.setVisible(true);
                    return;
                }
                double interestRate = 0;
                if(targetMoneyAccount instanceof SavingAccount)
                {
                    interestRate = Bank.getSavingAccountDepositInterestRate();
                }
                if(targetMoneyAccount instanceof CheckingAccount)
                {
                    interestRate = Bank.getCheckingAccountDepositInterestRate();
                }

                Balance targetBalance = null;
                //Search the target balance of target account.
                for(Balance item : targetMoneyAccount.getBalance())
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
                    targetMoneyAccount.addNewBalance(newBalance);
                    targetBalance = newBalance;
                }
                targetMoneyAccount.transferIn(targetBalance, interestRate, transferAmount, moneyAccount);

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

    private MoneyAccount findAccount(String id)
    {
        MoneyAccount targetMoneyAccount = null;
        for(Customer customer : bank.getCustomers())
        {
            for(Account account : customer.getAccounts())
            {
                if(account.getAccountID().equals(id) && (account instanceof MoneyAccount))
                {
                    targetMoneyAccount = (MoneyAccount)account;
                    return targetMoneyAccount;
                }
            }
        }

        return targetMoneyAccount;
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
