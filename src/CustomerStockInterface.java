import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CustomerStockInterface extends JFrame
{
    private Customer customer;
    private Bank bank;
    private CustomerStockInterface self = this;
    private CustomerHomePage father;
    private SavingAccount currentSavingAccount = null;
    private SecureAccount currentSecureAccount = null;

    private JComboBox<SecureAccount> secureAccountJComboBox = new JComboBox<SecureAccount>();
    private JComboBox<SavingAccount> savingAccountJComboBox = new JComboBox<SavingAccount>();
    private JComboBox<Balance> balanceJComboBox = new JComboBox<Balance>();
    private DefaultTableModel stockTableModel = new DefaultTableModel();
    private JTable stockTable = new JTable();
    private DefaultTableModel selfStockTableModel = new DefaultTableModel();
    private JTable selfStockTable = new JTable();
    private JButton jbtBuy = new JButton("Buy");
    private JButton jbtSell = new JButton("Sell");
    private JButton jbtOpenSecureAccount = new JButton("Open Secure Account");

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

        JLabel secureAccountLabel = new JLabel("Secure Account");
        JLabel savingAccountLabel = new JLabel("Saving Account");

        Dimension JComboBoxSize = new Dimension(250,20);
        secureAccountJComboBox.setEditable(false);
        secureAccountJComboBox.setEnabled(true);
        secureAccountJComboBox.setPreferredSize(JComboBoxSize);
        secureAccountJComboBoxListener secureAccountJComboBoxL = new secureAccountJComboBoxListener();
        secureAccountJComboBox.addItemListener(secureAccountJComboBoxL);
        savingAccountJComboBox.setEditable(false);
        savingAccountJComboBox.setEnabled(true);
        savingAccountJComboBox.setPreferredSize(JComboBoxSize);
        savingAccountJComBoxListener savingAccountJComBoxL = new savingAccountJComBoxListener();
        savingAccountJComboBox.addItemListener(savingAccountJComBoxL);
        List<Account> Accounts = customer.getAccounts();
        for(Account item : Accounts)
        {
            if(item instanceof SavingAccount)
            {
                SavingAccount savingAccount = (SavingAccount)item;
                savingAccountJComboBox.addItem(savingAccount);
            }
            if(item instanceof SecureAccount)
            {
                SecureAccount secureAccount = (SecureAccount)item;
                secureAccountJComboBox.addItem(secureAccount);
            }
        }

        GridBagLayout secureAccountLayout = new GridBagLayout();
        GridBagConstraints secureAccountG = new GridBagConstraints();
        JPanel secureAccountPanel = new JPanel(secureAccountLayout);
        secureAccountG.insets = new Insets(10,5,20,10);
        secureAccountLayout.addLayoutComponent(secureAccountLabel, secureAccountG);
        secureAccountLayout.addLayoutComponent(secureAccountJComboBox, secureAccountG);
        secureAccountPanel.add(secureAccountLabel);
        secureAccountPanel.add(secureAccountJComboBox);

        GridBagLayout savingAccountLayout = new GridBagLayout();
        GridBagConstraints savingAccountG = new GridBagConstraints();
        JPanel savingAccountPanel = new JPanel(savingAccountLayout);
        savingAccountG.insets = new Insets(10,5,20,10);
        savingAccountLayout.addLayoutComponent(savingAccountLabel, savingAccountG);
        savingAccountLayout.addLayoutComponent(savingAccountJComboBox, savingAccountG);
        savingAccountPanel.add(savingAccountLabel);
        savingAccountPanel.add(savingAccountJComboBox);

        JLabel stocksLabel = new JLabel("Stocks");

        JTableHeader stockHeader = stockTable.getTableHeader();
        stockHeader.setResizingAllowed(true);
        JScrollPane stockScroll = new JScrollPane(stockTable);
        String[] stockHeaderName = {"ID", "Name", "Price"};
        stockTableModel.setColumnIdentifiers(stockHeaderName);
        stockTable.setModel(stockTableModel);
        stockTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshStockTable();

        JLabel selfStocksLabel = new JLabel("Self Stocks");

        JTableHeader selfStockHeader = selfStockTable.getTableHeader();
        selfStockHeader.setResizingAllowed(true);
        JScrollPane selfStockScroll = new JScrollPane(selfStockTable);
        String[] selfStockHeaderName = {"Stock ID", "Name", "Buy In", "Amount", "Current Price"};
        selfStockTableModel.setColumnIdentifiers(selfStockHeaderName);
        selfStockTable.setModel(selfStockTableModel);
        selfStockTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshSelfStockTable();

        Dimension buttonSize = new Dimension(75,25);
        jbtBuy.setPreferredSize(buttonSize);
        BuyListener buyL = new BuyListener();
        jbtBuy.addActionListener(buyL);
        jbtSell.setPreferredSize(buttonSize);
        SellListener sellL = new SellListener();
        jbtSell.addActionListener(sellL);
        jbtOpenSecureAccount.setPreferredSize(new Dimension(160,25));
        OpenSecureAccountListener openSecureAccountL = new OpenSecureAccountListener();
        jbtOpenSecureAccount.addActionListener(openSecureAccountL);

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = null;
        JPanel panel = new JPanel(gridBag);

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(12,5,12,5);
        gridBag.addLayoutComponent(secureAccountPanel, c);
        gridBag.addLayoutComponent(savingAccountPanel, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(12,5,6,5);
        gridBag.addLayoutComponent(stocksLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(selfStocksLabel, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.addLayoutComponent(stockScroll, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(selfStockScroll, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10,5,20,5);
        gridBag.addLayoutComponent(jbtBuy, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(jbtSell, c);
        c.insets = new Insets(7,5,30,5);
        gridBag.addLayoutComponent(jbtOpenSecureAccount, c);

        panel.add(secureAccountPanel);
        panel.add(savingAccountPanel);
        panel.add(stocksLabel);
        panel.add(selfStocksLabel);
        panel.add(stockScroll);
        panel.add(selfStockScroll);
        panel.add(jbtBuy);
        panel.add(jbtSell);
        panel.add(jbtOpenSecureAccount);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    class secureAccountJComboBoxListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {
                SecureAccount selectAccount = (SecureAccount) e.getItem();
                String accountID = selectAccount.getAccountID();
                for(Account item : customer.getAccounts())
                {
                    if((item instanceof SecureAccount) && item.getAccountID().equals(accountID))
                    {
                        currentSecureAccount = (SecureAccount) item;
                        break;
                    }
                }
            }
        }
    }

    class savingAccountJComBoxListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {
                SavingAccount selectAccount = (SavingAccount) e.getItem();
                String accountID = selectAccount.getAccountID();
                for(Account item : customer.getAccounts())
                {
                    if((item instanceof SavingAccount) && item.getAccountID().equals(accountID))
                    {
                        currentSavingAccount = (SavingAccount) item;
                        break;
                    }
                }
            }
        }
    }

    class BuyListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
        }
    }

    class SellListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    }

    class OpenSecureAccountListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    }

    public void refreshStockTable()
    {
        stockTableModel.setRowCount(0);
        for(Stock item : bank.getStocks())
        {
            String id = item.getId();
            String name = item.getName();
            //String number = String.valueOf(item.getNumber());
            String price = String.format(" %.2f", item.getPrice());
            stockTableModel.addRow(new String[]{id, name, price});
        }
    }

    public void refreshSelfStockTable()
    {}

    private void closeFrame()
    {
        dispose();
        father.setVisible(true);
        toFront();
    }
}
