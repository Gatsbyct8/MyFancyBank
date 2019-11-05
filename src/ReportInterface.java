import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ReportInterface extends JFrame
{
    Bank bank;
    ManagerHomePage father;

    private DefaultTableModel transactionTableModel = new DefaultTableModel();
    private JTable transactionTable = new JTable();
    private DefaultTableModel incomeTableModel = new DefaultTableModel();
    private JTable incomeTable = new JTable();
    ReportInterface(Bank bank, ManagerHomePage father)
    {
        this.bank = bank;
        this.father = father;

        setTitle("Manger");
        setSize(900,700);
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

        JTableHeader transactionHeader = transactionTable.getTableHeader();
        transactionHeader.setResizingAllowed(true);
        JScrollPane transactionScroll = new JScrollPane(transactionTable);
        String[] transactionHeaderName = {"Account", "Currency", "Amount","Source\\Target", "Date"};
        transactionTableModel.setColumnIdentifiers(transactionHeaderName);
        transactionTable.setModel(transactionTableModel);
        transactionTable.setEnabled(false);
        refreshTransactionTable();

        JTableHeader incomeHeader = incomeTable.getTableHeader();
        incomeHeader.setResizingAllowed(true);
        JScrollPane incomeScroll = new JScrollPane(incomeTable);
        String[] incomeHeaderName = {"Account", "Amount", "Date", "Reason"};
        incomeTableModel.setColumnIdentifiers(incomeHeaderName);
        incomeTable.setModel(incomeTableModel);
        incomeTable.setEnabled(false);
        refreshincomeTable();

        add(transactionScroll);
        add(incomeScroll);
    }

    public void refreshTransactionTable()
    {
        transactionTableModel.setRowCount(0);
        for(Customer customer : bank.getCustomers())
        {
            for (Account account : customer.getAccounts()) {
                for (Balance balance : account.getBalance()) {
                    for (Transaction transaction : balance.getTransactions()) {
                        String acc = account.getAccountID();    // account ID
                        String currencyType = balance.getCurrencyType();
                        String amount = String.format(" %.2f", transaction.getAmount());
                        String sNtID = transaction.getSourceNtargetID();    // source or target ID
                        String date = transaction.getDate().toString();
                        transactionTableModel.addRow(new String[]{acc, currencyType, amount, sNtID, date});
                    }
                }
            }
        }
    }

    public void refreshincomeTable()
    {
        for(Transaction item : Bank.getIncome())
        {
            String acc = item.getSourceNtargetID();    // source ID
            String amount = String.format(" %.2f", item.getAmount());
            String date = item.getDate().toString();
            String reason = item.getReason();
            incomeTableModel.addRow(new String[]{acc, amount, date, reason});
        }
    }

    private void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }
}
