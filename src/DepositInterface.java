import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

public class DepositInterface extends JFrame
{
    private Account account;
    private Balance balance;
    private AccountInterface father;

    private JTextField amountField=new JTextField(4);

    DepositInterface(AccountInterface father, Account account, Balance balance)
    {
        this.father = father;
        this.account = account;
        this.balance = balance;

        setTitle("Deposit");
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

        JLabel savingAccountInterestRate = new JLabel("SavingAccountInterestRate: " + String.valueOf(Bank.getSavingAccountDepositInterestRate()));
        JLabel checkingAccountInterestRate = new JLabel("CheckingAccountInterestRate: " + String.valueOf(Bank.getCheckingAccountDepositInterestRate()));

        JButton jbtOK = new JButton("OK");
        okListener okL = new okListener();
        jbtOK.addActionListener(okL);

        add(amountPanel);
        add(savingAccountInterestRate);
        add(checkingAccountInterestRate);
        add(jbtOK);
    }

    class okListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(isValid(amountField.getText()))
            {
                double depositAmount = Double.valueOf(amountField.getText());
                if(account instanceof SavingAccount)
                {
                    account.deposit(balance, Bank.getSavingAccountDepositInterestRate(), depositAmount);
                }
                if(account instanceof CheckingAccount)
                {
                    account.deposit(balance, Bank.getCheckingAccountDepositInterestRate(), depositAmount);
                }

                closeFrame();
            }
            else
            {
                MessageDialog amountError = new MessageDialog("Error", "Amount invalid!");
                amountError.setVisible(true);
            }
        }
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
        father.setEnabled(true);
        father.refreshTransactionTable();
        dispose();
    }
}
